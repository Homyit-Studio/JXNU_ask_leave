<template>
	<view class="grade-content">
		<view class="grade-select">
			<uni-data-select v-model="gradevalue" :localdata="gradeSelect" @change="changeGrade" label="年级">
			</uni-data-select>
		</view>
		<view class="card-title">
			<uni-card :is-shadow="false" is-full>
				<text class="uni-h6">假条</text>
			</uni-card>
		</view>
		<view class="look-list">
			<view class="handle-leave">
				<view class="menu">
					<uni-data-menu :localdata="localMenus" :unique-opened="true" @select="changeMenu"
						active-text-color="#409eff">
					</uni-data-menu>
				</view>
				<uni-list class="leave-list">
					<uni-list-item class="leave-list-item" direction="column" v-for="(item, index) in leaveNoteList"
						:key="item.id + item.startTime">
						<template v-slot:header>
							<view class="card-header">
								<uni-tag v-if="item.examine == 'FAILURE'" :mark="true" :text="index+1+ '.'"
									type="error">
								</uni-tag>
								<uni-tag :mark="true" v-else :text="index+1 + '.'" type="default"></uni-tag>
								<view>
									<text>发起时间:{{item.startTime}}</text>
								</view>
							</view>
						</template>
						<template v-slot:body>
							<view><text decode="true">姓名: {{item.username}}</text></view>
							<view><text decode="true">班级: {{item.majorAndClass}}</text></view>
							<view><text decode="true">学号: {{item.studentNumber}}</text></view>
							<view><text>是否离校: {{item.depart == 'YES'? '是' :'否'}}</text></view>
							<view><text>请假时长: {{item.days}}</text></view>
						</template>
						<template v-slot:footer>
							<view class="card-actions">
								<view class="card-actions-item" @click="checkDetails(item.id, item.examine,index+1)">
									<view class="tag-view">
										<uni-tag
											:text="statuschoose == 1 && currentCatalog == 'PROCESSING' && (identity == item.level || item.level == 'INSTRUCTOR' || item.examine == 'INSTRUCTOR')? '去审批' : '查看详情'"
											custom-style="background-color: #1b478e; border-color: #1b478e; color: #fff;" />
									</view>
								</view>
							</view>
						</template>
					</uni-list-item>
					<view v-if="shownodata">
						<view class="show-nodata"><text>没有更多数据了</text></view>
					</view>
				</uni-list>
			</view>
		</view>
		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msg.msgType" :message="msg.messageText" :duration="2000">
				</uni-popup-message>
			</uni-popup>
		</view>
		<view class="goTotop" v-if="showTop" @click="gobackTop">
			<uni-icons type="top" color="#fff" size="15"></uni-icons>
			顶部
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				showTop: false,
				currentPage: 1,
				currentCatalog: "PROCESSING",
				pageId: "grade",
				statuschoose: null,
				identity: null,

				changeCart: true,
				localMenus: [{
						total: null,
						text: '等待处理',
						value: "PROCESSING",
					}, {
						total: null,
						text: '销假完成',
						value: "PROCESSED",
					},
					{
						total: null,
						text: '等待销假',
						value: "WAIT_REPORT",
					},
					{
						total: null,
						text: '销假过期',
						value: "REPORT_EXPIRED",
					},
					{
						total: null,
						text: '申请过期',
						value: "APPLY_EXPIRED",
					},
					{
						total: null,
						text: '已拒假条',
						value: "FAILURE",
					}
				],
				//没有更多数据提醒
				shownodata: false,
				gradevalue: "2021",
				gradeSelect: [{
						value: "2021",
						text: "2021级"
					},
					{
						value: "2020",
						text: "2020级"
					},
					{
						value: "2019",
						text: "2019级"
					},
					{
						value: "2018",
						text: "2018级"
					},
					{
						value: "3021",
						text: "研一"
					},
					{
						value: "3020",
						text: "研二"
					},
					{
						value: "3019",
						text: "研三"
					},
					{
						value: "1018",
						text: "留学生四年级"
					},
					{
						value: "1019",
						text: "留学生三年级"
					}
				],
				isloading: false,
				//数据总数
				endPage: null,
				msg: {
					msgType: 'success',
					messageText: '这是一条成功提示',
				},
				currentIndex: 0,
				handleChoices: ["未处理", "已处理"],
				leaveNoteList: [],
				listRequest: {
					"pageNo": 1,
					"pageSize": 5,
					"examineEnum": "PROCESSING",
					"gradeId": "2021"
				}
			}
		},
		onLoad(options) {
			this.statuschoose = options.choose;
			// this.requestLeaveNotes()
			// this.requestLeaveCount()
		},
		onReady() {
			uni.setStorageSync('pageNoDetail' + this.pageId, 1);
			this.identity = uni.getStorageSync("identity")
		},
		onShow() {

			// this.listRequest.pageNo = 1
			// if(this.changeCart){
			// 	this.requestLeaveNotes()
			// 	setTimeout(() => {
			// 		this.changeCart = false
			// 	}, 500)
			// }

			if (this.listRequest.examineEnum == "PROCESSING") {
				this.requestLeaveCount()
				this.getscrollTop()
			}
		},
		methods: {
			changeGrade(grade) {
				this.listRequest.gradeId = grade
				this.listRequest.pageNo = 1
				this.changeCart = true
				this.requestLeaveCount()
				this.requestLeaveNotes()
				setTimeout(() => {
					this.changeCart = false
				}, 1000)

			},
			onClickChoice(index) {
				if (index.currentIndex == 0) {
					this.listRequest.pageNo = 1
					this.shownodata = false
					this.requestLeaveNotes()
				} else {
					this.listRequest.pageNo = 1
					this.shownodata = false
					this.requestLeaveNotes()
				}
			},
			requestLeaveCount() {

				uni.$http.post(`/leave/allCountsFroGradeId`, {
					startTime: "2021-07-26 03:34:26",
					endTime: this.getFormatDate(),
					gradeId: this.listRequest.gradeId
				}).then(res => {
					// console.log(res)
					if (res.data.code == 200) {
						uni.showToast({
							title: '加载中',
							duration: 1000,
							icon: "loading"
						});
						let data = res.data.data
						for (let index in this.localMenus) {
							// console.log(data[this.localMenus[index].value])
							this.localMenus[index].total = data[this.localMenus[index].value]
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = "请求假条数据错误"
						this.$refs.message.open()
					}
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err.errMsg
					this.$refs.message.open()
				})
			},
			requestLeaveNotes() {
				console.log("再次执行")
				uni.showToast({
					title: '加载中',
					duration: 1000,
					icon: "loading"
				});
				uni.$http.post("/leave/selectNodeByGrade", this.listRequest).then(res => {
					if (res.data.code == 200) {
						console.log(res)
						this.leaveNoteList = res.data.data.list
						console.log(res.data.data.endPage)
						console.log(this.listRequest)
						if (this.changeCart) {

							this.endPage = res.data.data.endPage
							console.log("end", this.endPage)

						}

						setTimeout(() => {
							this.changeCart = false
						}, 1000)

						if (this.listRequest.pageNo >= this.endPage) {
							this.shownodata = true
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = "数据请求错误，请重试"
						this.$refs.message.open()
						this.shownodata = true
					}
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err.errMsg
					this.$refs.message.open()
					this.shownodata = true
				})
			},
			//格式化时间
			getFormatDate(choose) {
				var date = new Date();
				var sign1 = "-";
				var sign2 = ":";
				var year = date.getFullYear() // 年
				var month = date.getMonth() + 1; // 月
				var day = date.getDate(); // 日
				var hour = date.getHours(); // 时
				var minutes = date.getMinutes(); // 分
				var seconds = date.getSeconds() //秒
				// 给一位数数据前面加 “0”
				if (month >= 1 && month <= 9) {
					month = "0" + month;
				}
				if (choose == 2) {
					day -= 7
				}
				if (day >= 0 && day <= 9) {
					day = "0" + day;
				}
				if (hour >= 0 && hour <= 9) {
					hour = "0" + hour;
				}
				if (minutes >= 0 && minutes <= 9) {
					minutes = "0" + minutes;
				}
				if (seconds >= 0 && seconds <= 9) {
					seconds = "0" + seconds;
				}
				var currentdate = year + sign1 + month + sign1 + day + " " + hour + sign2 + minutes + sign2 + seconds;
				return currentdate;
			},
			checkDetails(id, examine, indexnum) {
				this.currentPage = Math.ceil(indexnum / 5)
				uni.setStorageSync('pageNoDetail' + this.pageId, this.currentPage);
				uni.navigateTo({
					url: `../handleLeaveDetail/handleLeaveDetail?id=${id}&current=${this.currentCatalog}&card=${this.statuschoose}`,
					animationType: 'pop-in',
					animationDuration: 200
				})
			},
			changeMenu(e) {
				this.listRequest.pageNo = 1
				this.listRequest.examineEnum = e.value;
				this.currentCatalog = e.value
				this.changeCart = true
				this.requestLeaveNotes()
			},
			throttle(fn, delay) {
				let t = null,
					begin = new Date().getTime();
				return function() {
					let _self = this,
						args = arguments,
						cur = new Date().getTime();
					clearTimeout(t)

					if (cur - begin >= delay) {
						console.log(cur)
						fn.apply(_self, args);
						begin = cur
					} else {
						uni.showToast({
							icon: "error",
							title: "操作过快，请稍等"
						})
						t = setTimeout(function() {
							console.log("set")
							console.log(cur)
							fn.apply(_self, args)
						}, delay)
					}
				}
			},

			gobackTop() {
				uni.pageScrollTo({
					duration: 500, // 毫秒
					scrollTop: 1 // 位置
				})
			},
			getscrollTop() {
				let scrollDetail = uni.getStorageSync('scrollDetail' + this.pageId)
				let pageNoDetail = uni.getStorageSync('pageNoDetail' + this.pageId)


				console.log(this.listRequest)
				console.log(typeof pageNoDetail)

				if (!pageNoDetail) {
					pageNoDetail = 1
				}

				console.log(scrollDetail, pageNoDetail)
				this.listRequest.pageSize = pageNoDetail * 5
				this.listRequest.pageNo = 1
				console.log("执行")
				uni.$http.post("/leave/selectNodeByGrade", this.listRequest).then(res => {
					if (res.data.code == 200) {
						console.log(res)
						this.leaveNoteList = res.data.data.list
						if (this.listRequest.pageNo >= this.endPage) {
							this.shownodata = true
						}
						if (this.changeCart) {

							this.endPage = res.data.data.endPage
							console.log("end", this.endPage)

						}

						setTimeout(() => {
							this.changeCart = false
						}, 1000)
						this.listRequest.pageSize = 5
						this.listRequest.pageNo = pageNoDetail
						uni.pageScrollTo({
							duration: 500, // 毫秒
							scrollTop: scrollDetail // 位置
						})
						// uni.setStorageSync('scrollDetail' + this.pageId, 1);
						// uni.setStorageSync('pageNoDetail' + this.pageId, 1);
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
						this.shownodata = true
						// uni.setStorageSync('scrollDetail' + this.pageId, 1);
						// uni.setStorageSync('pageNoDetail' + this.pageId, 1);
					}
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err.errMsg
					this.$refs.message.open()
					this.shownodata = true
					// uni.setStorageSync('scrollDetail' + this.pageId, 1);
					// uni.setStorageSync('pageNoDetail' + this.pageId, 1);
				})

				// if (data) {
				// 	uni.pageScrollTo({
				// 		duration: 500, // 毫秒
				// 		scrollTop: data // 位置
				// 	})
				// }
			},
		},
		onReachBottom() {
			console.log(this.endPage)
			// console.log(this.listRequest.pageNo, this.endPage)
			if (this.listRequest.pageNo >= this.endPage) {
				this.shownodata = true
				return
			}
			if (this.isloading) return;
			this.isloading = true
			this.listRequest.pageNo++;
			this.throttle(uni.$http.post(`/leave/selectNodeByGrade`, this.listRequest).then(res => {
				if (res.data.code == 200) {
					this.leaveNoteList = [...this.leaveNoteList, ...res.data.data.list]
					this.isloading = false
				} else {
					//如果请求错误的话，重新请求一次
					this.listRequest.pageNo--;
					this.msg.msgType = "none"
					this.msg.messageText = "错误！操作频繁请重试"
					this.$refs.message.open()
					this.isloading = false
				}
			}), 1000)
		},
		// 监听页面滚动位置
		onPageScroll(e) {
			// console.log(e) // {scrollTop: 216}
			// console.log(this.listRequest.pageNo)
			this.scrollTop = e.scrollTop;
			this.scrollTop = e.scrollTop;
			if (e.scrollTop >= 1000) {
				this.showTop = true
			} else {
				this.showTop = false
			}
			uni.setStorageSync('scrollDetail' + this.pageId, e.scrollTop);

		},
		onPullDownRefresh() {
			this.listRequest.pageNo = 1
			this.changeCart = true
			this.requestLeaveNotes()
			this.requestLeaveCount()
			setTimeout(function() {
				uni.stopPullDownRefresh();
			}, 1000);
		}
	}
</script>

<style lang="scss">
	.grade-content {
		.grade-select {
			display: flex;
			justify-content: center;
		}

		.card-title {
			text-align: center;
		}

		.handle-leave {
			display: flex;

			.card-actions-item {
				display: flex;
				justify-content: flex-end;
				align-items: center;
				color: $jxnu-bg-color;
			}

			.card-header {
				display: flex;
				justify-content: space-between;
				margin-top: 10rpx;
				color: #666;
			}
		}

		.look-list {
			display: flex;
			// .menu{
			// 	position: sticky;
			// 	top: 0;
			// 	z-index: 1000;
			// }
		}

		.leave-list {
			.leave-list-item {
				width: 83vw;
			}

			.card-header {
				font-size: 12px;
			}

			text {
				font-size: 12px;
			}
		}

		.show-nodata {
			text-align: center;
			padding: 20px;
		}

		.goTotop {
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			width: 30px;
			height: 30px;
			background-color: #bcbcbc;
			padding: 10px;
			border-radius: 50px;
			color: #fff;
			position: fixed;
			bottom: 20px;
			right: 20px;
			z-index: 1000;
			font-size: 12px;
		}
	}
</style>
