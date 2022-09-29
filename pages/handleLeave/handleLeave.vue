<template>
	<view class="handle-leave-page">
		<view>
			<view class="leave-remind-title">
				<uni-card :is-shadow="false" is-full>
					<text class="card-title">假条</text>
				</uni-card>
			</view>
			<view class="leave-notes">
				<view>
					<uni-data-menu :localdata="localMenus" :unique-opened="true" active-text-color="#409eff"
						@select="changeMenu">
					</uni-data-menu>
				</view>
				<view>
					<uni-list class="leave-list">
						<template v-for="(item, index) in leaveNoteList">
							<uni-list-item class="leave-list-item" direction="column">
								<template v-slot:header>
									<view class="card-header">
										<uni-tag v-if="item.examine == 'FAILURE'" :mark="true" :text="index+1+ '.'"
											type="error">
										</uni-tag>
										<uni-tag :mark="true" v-else :text="index+1 + '.'" type="default"></uni-tag>
										<view>
											<text>请假开始时间:{{item.startTime}}</text>
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
										<view class="card-actions-item"
											@click="checkDetails(item.id,item.examine,index+1)">
											<view class="tag-view">
												<uni-tag :text="currentCatalog == 'PROCESSING'? '去审批' : '查看详情'"
													custom-style="background-color: #1b478e; border-color: #1b478e; color: #fff;" />
											</view>
										</view>
									</view>
								</template>
							</uni-list-item>
						</template>
					</uni-list>
					<view class="show-nodata" v-if="shownodata"><text>没有更多数据了</text></view>
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
	</view>
</template>

<script>
	export default {
		data() {
			return {
				//当前分类
				currentCatalog: "PROCESSING",
				pageId: "teacher",
				currentPage: 1,
				showTop: false,
				changeCart: true,
				localMenus: [{
						total: null,
						text: '等待处理',
						value: "PROCESSING",
					},
					{
						total: null,
						text: '上级审核',
						value: "TRANSMIT",
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
					"examineEnum": "PROCESSING"
				}

			}
		},
		onShow() {
			if (this.listRequest.examineEnum == "PROCESSING") {
				this.requestLeaveCount()
				this.getscrollTop()
			}
		},
		methods: {
			onClickChoice(index) {
				if (index.currentIndex == 0) {
					this.shownodata = false
					this.listRequest.pageNo = 1
					this.requestLeaveNotes()
				} else {
					this.listRequest.pageNo = 1
					this.shownodata = false
					this.requestLeaveNotes()
				}
			},
			requestLeaveCount() {
				uni.showLoading({
					title: "正在获取数据中"
				})
				uni.$http.get("/leave/allCountsForPerson").then(res => {
					if (res.data.code == 200) {
						uni.hideLoading()

						let data = res.data.data
						for (let index in this.localMenus) {
							// console.log(data[this.localMenus[index].value])
							this.localMenus[index].total = data[this.localMenus[index].value]
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = "请求错误"
						this.$refs.message.open()
					}
				})
			},
			requestLeaveNotes() {
				uni.showLoading({
					title: "正在获取请假数据"
				})
				uni.$http.post("/leave/selectNoteByRole", this.listRequest).then(res => {
					if (res.data.code == 200) {
						this.leaveNoteList = res.data.data.list
						console.log(this.leaveNoteList)
						if (this.changeCart) {
							this.endPage = res.data.data.endPage
							console.log("end", this.endPage)
						}

						setTimeout(() => {
							this.changeCart = false
						}, 1000)
						uni.hideLoading()
						if (this.listRequest.pageNo >= this.endPage) {
							this.shownodata = true
						}
					} else {
						this.shownodata = true
						this.msg.msgType = "error"
						this.msg.messageText = "请求失败"
						this.$refs.message.open()
					}
				})
			},
			// 查看详情
			checkDetails(id, examine, indexnum) {
				console.log(id)
				this.currentPage = Math.ceil(indexnum / 5)
				uni.setStorageSync('pageNoDetail' + this.pageId, this.currentPage);
				uni.navigateTo({
					url: `../handleLeaveDetail/handleLeaveDetail?id=${id}&current=${this.currentCatalog}&card=3`,
					animationType: 'pop-in',
					animationDuration: 200
				})
			},
			changeMenu(e) {
				this.currentCatalog = e.value;
				this.listRequest.pageNo = 1
				this.listRequest.examineEnum = e.value;
				this.leaveNoteList = []
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
				console.log(this.listRequest)
				if (!pageNoDetail) {
					pageNoDetail = 1
				}

				console.log(scrollDetail, pageNoDetail)
				this.listRequest.pageSize = pageNoDetail * 5
				this.listRequest.pageNo = 1
				console.log("执行")
				console.log(this.listRequest)
				uni.$http.post("/leave/selectNoteByRole", this.listRequest).then(res => {
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
	.handle-leave-page {
		display: flex;

		.card-title {
			text-align: center;
		}

		.card-actions-item {
			display: flex;
			justify-content: flex-end;
			align-items: center;
			color: $jxnu-bg-color;
			font-size: 12px;
		}

		.card-header {
			display: flex;
			justify-content: space-between;
			margin-top: 10rpx;
			color: #666;
		}

		.show-nodata {
			text-align: center;
			padding: 20px;
		}

		.leave-remind-title {
			width: 100vw;
			text-align: center;
		}


		.leave-notes {
			display: flex;
			justify-content: flex-start;
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

		.goTotop {
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			width: 30px;
			height: 30px;
			background-color: #bcbcbc;
			color: #fff;
			padding: 10px;
			border-radius: 50px;
			position: fixed;
			bottom: 20px;
			right: 20px;
			z-index: 1000;
			font-size: 12px;
		}
	}
</style>
