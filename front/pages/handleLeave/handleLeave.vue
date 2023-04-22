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
							<!-- 这里不能加key 系统diff算法有问题 -->
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
										<!-- 未审批假条 辅导员可以批量审三天以内的 负责人院长都可以申 -->
										<label v-if="currentCatalog== 'PROCESSING'">
											<checkbox
												v-if="(identity == 'INSTRUCTOR' && parseInt(item.days.substr(0,2)) < 3) || (identity == 'SECRETARY' && isThreeToSeven && parseInt(item.days.substr(0,2)) < 7) || (identity == 'SECRETARY' && !isThreeToSeven && parseInt(item.days.substr(0,2)) >= 7) || identity == 'DEAN'"
												style="transform:scale(0.7)" checked={{item.checked}} @click="changeCheck(item.id)" value="checkbox" />
										</label>
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
			<view>
				<!-- 批量审批弹框 -->
				<uni-popup ref="verifyDialog" type="dialog">
					<template v-slot:default>
						<view class="confirm-dialog">
							<uni-forms :modelValue="processMessage" ref="reviceMessage" label-position="top"
								label-width="90" :rules="commitRules">
								<uni-forms-item name="advice" label="审批建议">
									<uni-easyinput type="textarea" v-model="processMessage.teacherOpinion"
										placeholder="请输入审核意见" />
								</uni-forms-item>
							</uni-forms>
							<view class="confirm-button">
								<button plain size="mini" @click="refuseConfirm()">拒绝</button>
								<button plain size="mini" @click="agreeConfirm()">同意</button>
							</view>
						</view>
					</template>
				</uni-popup>
			</view>
			<view class="batch-button" v-if="currentCatalog== 'PROCESSING'">
				<!-- 负责人要判断7天以内还是7天以外 -->
				<uni-data-checkbox v-model="isThreeToSeven" :localdata="daysLimits" v-if="identity == 'SECRETARY'">
				</uni-data-checkbox>
				<button @click="batchOperate">批量审批</button>
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
				isThreeToSeven: true,
				//当前分类
				currentCatalog: "PROCESSING",
				// 老师审核意见
				processMessage: {
					teacherOpinion: ""
				},
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
				},
				// 身份
				identity: null,
				daysLimits: [{
						text: "7天以内",
						value: true
					},
					{
						text: "7天以上",
						value: false
					}
				],
				checkedList: []
			}
		},
		onShow() {
			if (this.listRequest.examineEnum == "PROCESSING") {
				this.requestLeaveCount()
				this.getscrollTop()
			}
		},
		onLoad() {
			this.identity = uni.getStorageSync('identity')
		},
		methods: {
			// 选择按钮
			changeCheck(id) {
				console.log(id)
				this.leaveNoteList.forEach(item => {
					if (item.id == id) {
						const {
							checked
						} = item
						item.checked = !checked
						return
					}
				})
			},
			// 批量审批
			batchOperate() {
				console.log(this.leaveNoteList)
				const checkedArr = []
				this.leaveNoteList.map(item => {
					if (item.checked) {
						checkedArr.push(item.id)
					}
				})
				if (checkedArr.length <= 0) {
					uni.showToast({
						title:"未选择假条",
						icon:"none"
					})
					return null
				}
				this.checkedList = checkedArr
				this.$refs.verifyDialog.open()
			},
			// 批量同意
			agreeConfirm() {
				let level = ""
				if (this.identity == "INSTRUCTOR") {
					level = "INSTRUCTOR"
				}
				if (this.identity == "SECRETARY" && this.isThreeToSeven) {
					level = "SECRETARY"
				}
				if (this.identity == "SECRETARY" && !this.isThreeToSeven) {
					level = "DEAN"
				}
				if (this.identity == 'DEAN') {
					level = "DEAN"
				}
				let status = this.identity.toLowerCase() + "Opinion"
				let requestMessage = {
					"ids": this.checkedList.join(),
					"levelEnum": level,
					"opinionEnum": "YES"
				}
				if (this.processMessage.teacherOpinion == "") {
					this.processMessage.teacherOpinion = "同意"
				}
				requestMessage[status] = this.processMessage.teacherOpinion
				console.log(requestMessage)
				uni.$http.post("/leave/updateLotsNote", requestMessage).then(res => {
					// console.log(res)
					if (res.data.code == 200) {
						this.requestLeaveCount()
						this.getscrollTop()

						uni.showToast({
							title: "批量审批成功",
							icon: "success"
						})
					} else {
						uni.showToast({
							title: "审批失败请重试",
							icon: "error"
						})
					}
				})
				this.processMessage.teacherOpinion = ""
				this.$refs.verifyDialog.close()
			},
			// 批量拒绝
			refuseConfirm() {
				let level = ""
				if (this.identity == "INSTRUCTOR") {
					level = "INSTRUCTOR"
				}
				if (this.identity == "SECRETARY" && this.isThreeToSeven) {
					level = "SECRETARY"
				}
				if (this.identity == "SECRETARY" && !this.isThreeToSeven) {
					level = "DEAN"
				}
				if (this.identity == 'DEAN') {
					level = "DEAN"
				}
				let status = this.identity.toLowerCase() + "Opinion"
				let requestMessage = {
					"ids": this.checkedList.join(),
					"levelEnum": level,
					"opinionEnum": "NO"
				}
				if (this.processMessage.teacherOpinion == "") {
					console.log("空")
					this.processMessage.teacherOpinion = "拒绝"
				}
				requestMessage[status] = this.processMessage.teacherOpinion
				uni.$http.post("/leave/updateLotsNote", requestMessage).then(res => {
					// console.log(res)
					if (res.data.code == 200) {
						this.requestLeaveCount()
						this.getscrollTop()
						uni.showToast({
							title: "批量审批成功",
							icon: "success"
						})
					} else {
						uni.showToast({
							title: "审批失败请重试",
							icon: "error"
						})
					}
				})
				this.processMessage.teacherOpinion = ""
				this.$refs.verifyDialog.close()
			},
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
			// 获取各假条条数
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
			// 请求假条数据
			requestLeaveNotes() {
				uni.showLoading({
					title: "正在获取请假数据"
				})
				uni.$http.post("/leave/selectNoteByRole", this.listRequest).then(res => {
					if (res.data.code == 200) {
						this.leaveNoteList = res.data.data.list
						// 如果改变分类的话重新获取假条总数 默认值是true
						if (this.changeCart) {
							this.endPage = res.data.data.endPage
							console.log("end", this.endPage)
						}

						setTimeout(() => {
							this.changeCart = false
						}, 200)
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
				// indexnum用来计算当前的pageNo
				this.currentPage = Math.ceil(indexnum / 5)
				uni.setStorageSync('pageNoDetail' + this.pageId, this.currentPage);
				uni.navigateTo({
					url: `../handleLeaveDetail/handleLeaveDetail?id=${id}&current=${this.currentCatalog}&card=3`,
					animationType: 'pop-in',
					animationDuration: 200
				})
			},
			// 改变假条分类
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
			// 回到顶部
			gobackTop() {
				uni.pageScrollTo({
					duration: 500, // 毫秒
					scrollTop: 1 // 位置
				})
			},
			// 获得滚动位置
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
		// 上拉加载
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
		.card-actions{
			display: flex;
			justify-content: space-between;
			align-items: center;
		}

		.card-actions-item {
			flex: 1;
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

		.batch-button {
			box-sizing: border-box;
			position: fixed;
			bottom: 20px;
			left: 20px;
			z-index: 1000;
			padding: 10rpx;
			background-color: #e9e9e9;
			.uni-data-checklist .checklist-group {
				display: flex;
				flex-direction: column !important;
				align-items: center;
				margin-left: 20rpx;
			}

			button {
				width: 160rpx;
				height: 55rpx;
				line-height: 55rpx;
				font-size: 12px;
				margin-top: 10rpx;
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

		.confirm-dialog {
			width: 570rpx;
			padding: 10px 15px;
			border-radius: 10px;
			background-color: #fff;

			textarea {
				border: 1px solid #e2e2e2;
				width: 550rpx;
				border-radius: 5px;
				padding: 5px;
				margin-top: 5px;
				height: 150rpx;
			}

			.text {
				margin-bottom: 5px;
			}

			.confirm-button {
				display: flex;
				justify-content: center;
				padding-top: 10rpx;
			
				button {
					width: 250rpx;
					height: 60rpx;
					border: none;
					line-height: 60rpx;
					font-size: $jxnu-font-14;
				}
			}
		}
	}
</style>
