<template>
	<view class="grade-content">
		<uni-notice-bar scrollable="true" single="true" text="为落实落细防疫工作,请各位同学在离校和返校后进行假条销假。如未出行，也请在假条销假界面中填写返校内容。 如果数据出现异常,请下拉刷新重试"
			showIcon></uni-notice-bar>
		<view class="look-list">
			<view class="handle-leave">
				<view class="menu">
					<uni-data-menu :localdata="localMenus" :unique-opened="true" @select="changeMenu"
						:value="currentValue" active-text-color="#409eff">
					</uni-data-menu>
				</view>
				<uni-list class="leave-list">
					<uni-list-item class="leave-list-item" direction="column" v-for="(item, index) in leaveNoteList">
						<template v-slot:header>
							<view class="card-header">
								<uni-tag v-if="item.examine == 'SUCCESS'" :text=" index+1 + '.'" type="success">
								</uni-tag>
								<uni-tag v-else-if="item.examine == 'FAILURE'" :text="index+1+ '.'" type="error">
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
								<view class="card-actions-item" @click="checkDetails(item.id,index+1)">
									<view class="tag-view">
										<uni-tag :text="statuschoose == 1? '去审批' : '查看详情'"
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
				pageId: "student",
				currentPage: 1,
				changeCart: true,
				isloading: false,
				statuschoose: null,
				currentValue: 'PROCESSING',
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
						text: '被拒假条',
						value: "FAILURE",
					}
				],
				//没有更多数据提醒
				shownodata: false,
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
		onShow() {
			if (this.listRequest.examineEnum == 'WAIT_REPORT' || this.listRequest.examineEnum == 'REPORT_EXPIRED') {
				this.requestLeaveCount()
				this.getscrollTop()
			}

		},
		onLoad(options) {
			this.statuschoose = options.choose;
			//console.log(uni.getStorageSync('token'))
			//console.log(options)
			if (options.is_WAIT_REPORT == "true") {
				this.currentValue = "WAIT_REPORT";
				this.listRequest.examineEnum = "WAIT_REPORT";

			}
		},
		methods: {
			changeGrade(grade) {
				this.listRequest.gradeId = grade
				this.listRequest.pageNo = 1
				this.requestLeaveNotes()
				this.requestLeaveCount()
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
				uni.$http.get(`/leave/allCountsForPerson`).then(res => {
					if (res.data.code == 200) {
						let data = res.data.data
						for (let index in this.localMenus) {
							this.localMenus[index].total = data[this.localMenus[index].value]
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
					}
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err.errMsg
					this.$refs.message.open()
				})
			},
			requestLeaveNotes() {
				uni.$http.post("/leave/selectNoteByRole", this.listRequest).then(res => {
					if (res.data.code == 200) {
						uni.showToast({
							title: '加载中',
							duration: 500,
							icon: "loading"
						});

						// console.log(res.data.data.total)
						this.leaveNoteList = res.data.data.list
						if (this.changeCart) {
							this.endPage = res.data.data.endPage
							console.log("end", this.endPage)
						}

						setTimeout(() => {
							this.changeCart = false
						}, 1000)
						this.leaveNoteList = res.data.data.list
						this.endPage = res.data.data.endPage;
						//console.log(this.leaveNoteList)
						if (this.listRequest.pageNo >= this.endPage) {
							this.shownodata = true
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = "请求错误"
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
			checkDetails(id, indexnum) {
				this.currentPage = Math.ceil(indexnum / 5)
				uni.setStorageSync('pageNoDetail' + this.pageId, this.currentPage);
				uni.navigateTo({
					url: `/pages/allLeaveDetails/allLeaveDetails?id=` + id + '&type=' + this.currentValue,
					animationType: 'pop-in',
					animationDuration: 200
				})
			},
			changeMenu(e) {
				//console.log(this.activeUrl)
				// console.log(e)
				this.currentValue = e.value;
				this.listRequest.pageNo = 1
				this.listRequest.examineEnum = e.value;
				this.changeCart = true
				this.requestLeaveNotes()
			},
			//节流处理
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
				console.log(typeof pageNoDetail)

				if (!pageNoDetail) {
					pageNoDetail = 1
				}

				console.log(scrollDetail, pageNoDetail)
				this.listRequest.pageSize = pageNoDetail * 5
				this.listRequest.pageNo = 1
				console.log("执行")
				uni.$http.post("/leave/selectNoteByRole", this.listRequest).then(res => {
					if (res.data.code == 200) {
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
			if (this.listRequest.pageNo >= this.endPage) {
				this.shownodata = true
				return
			}
			if (this.isloading) return;
			this.isloading = true
			this.listRequest.pageNo++;
			this.throttle(uni.$http.post(`/leave/selectNoteByRole`, this.listRequest).then(res => {
				if (res.data.code == 200) {
					uni.showToast({
						title: '加载中',
						duration: 500,
						icon: "loading"
					});
					this.leaveNoteList = [...this.leaveNoteList, ...res.data.data.list]
					this.isloading = false
				} else {
					this.listRequest.pageNo--;
					this.msg.msgType = "none"
					this.msg.messageText = "错误！操作频繁请重试"
					this.$refs.message.open()
					this.isloading = false
				}
			}).catch(err => {
				this.msg.msgType = "error"
				this.msg.messageText = err.errMsg
				this.$refs.message.open()
				this.isloading = false
			}), 1000)
		},
		// 监听页面滚动位置
		onPageScroll(e) {
			// console.log(e) // {scrollTop: 216}
			// console.log(this.listRequest.pageNo)
			this.scrollTop = e.scrollTop;
			if (e.scrollTop >= 1000) {
				this.showTop = true
			} else {
				this.showTop = false
			}
			uni.setStorageSync('scrollDetail' + this.pageId, e.scrollTop);

		},
		onPullDownRefresh() {
			this.changeCart = true
			this.listRequest.pageNo = 1
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
			position: fixed;
			bottom: 20px;
			right: 20px;
			z-index: 1000;
			font-size: 12px;
			color: #fff;
		}
	}
</style>
