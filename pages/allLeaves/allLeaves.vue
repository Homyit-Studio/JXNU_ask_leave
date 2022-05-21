<template>
	<view class="grade-content">
	<!-- 	<view class="grade-select">
			<uni-data-select v-model="gradevalue" :localdata="gradeSelect" @change="changeGrade" label="年级">
			</uni-data-select>
		</view> -->
		<!-- <view class="card-title">
			<uni-card :is-shadow="false" is-full>
				<text class="uni-h6">假条</text>
			</uni-card>
		</view> -->
		<view class="look-list">
			<view class="handle-leave">
				<view class="menu">
					<uni-data-menu :localdata="localMenus" :unique-opened="true" @select="changeMenu"
						active-text-color="#409eff">
					</uni-data-menu>
				</view>
				<uni-list class="leave-list">
					<uni-list-item class="leave-list-item" direction="column" v-for="(item, index) in leaveNoteList"
						:key="item.id">
						<template v-slot:header>
							<view class="card-header">
								<uni-tag v-if="item.examine == 'SUCCESS'" :text=" index+1 + '.已同意'" type="success">
								</uni-tag>
								<uni-tag v-else-if="item.examine == 'FAILURE'" :text="index+1+ ',已拒绝'" type="error">
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
								<view class="card-actions-item" @click="checkDetails(item.id)">
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
	</view>
</template>

<script>
	export default {
		data() {
			return {
				statuschoose: null,
				currentValue:'PROCESSING',
				localMenus: [{
						total: null,
						text: '等待处理',
						value: "PROCESSING",
					},{
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
				// gradevalue: "2021",
				// gradeSelect: [{
				// 		value: "2021",
				// 		text: "2021级"
				// 	},
				// 	{
				// 		value: "2020",
				// 		text: "2020级"
				// 	}
				// ],
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
			console.log(uni.getStorageSync('token'))
			this.statuschoose = options.choose;
			this.requestLeaveNotes()
			//this.requestLeaveCount()
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
			// requestLeaveCount(){
			// 	uni.$http.get(`/leave/allCounts/${this.listRequest.gradeId}`).then(res => {
			// 		if (res.data.code == 200) {
			// 			let data = res.data.data
			// 			for (let index in this.localMenus) {
			// 				console.log(data[this.localMenus[index].value])
			// 				this.localMenus[index].total = data[this.localMenus[index].value]
			// 			}
			// 		} else {
			// 			this.msg.msgType = "error"
			// 			this.msg.messageText = res.data.message
			// 			this.$refs.message.open()
			// 		}
			// 	}).catch(err => {
			// 		this.msg.msgType = "error"
			// 		this.msg.messageText = err.errMsg
			// 		this.$refs.message.open()
			// 	})
			// },
			requestLeaveNotes(){
				uni.$http.post("/leave/selectNoteByRole", this.listRequest).then(res => {
					if (res.data.code == 200) {
						uni.showToast({
							title: '加载中',
							duration: 500,
							icon: "loading"
						});
						console.log(res.data.data.total)
						this.leaveNoteList = res.data.data.list
						this.endPage = res.data.data.endPage;
						console.log(this.leaveNoteList)
						if (this.listRequest.pageNo >= this.endPage) {
							this.shownodata = true
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
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
			checkDetails(id) {
				uni.navigateTo({
					url: `/pages/allLeaveDetails/allLeaveDetails?id=` + id + '&type=' + this.currentValue ,
					animationType: 'pop-in',
					animationDuration: 200
				})
			},
			changeMenu(e) {
				//console.log(e)
				this.currentValue = e.value;
				console.log(this.currentValue)
				this.listRequest.pageNo = 1
				this.listRequest.examineEnum = e.value;
				this.requestLeaveNotes()
			}
		},
		onReachBottom() {
			if (this.listRequest.pageNo >= this.endPage) {
				this.shownodata = true
				return
			}
			if (this.isloading) return;
			this.isloading = true
			this.listRequest.pageNo++;
			uni.$http.post(`/leave/selectNodeByGrade`, this.listRequest).then(res => {
				if (res.data.code == 200) {
					uni.showToast({
						title: '加载中',
						duration: 500,
						icon: "loading"
					});
					this.leaveNoteList = [...this.leaveNoteList, ...res.data.data.list]
					this.isloading = false
				} else {
					this.msg.msgType = "error"
					this.msg.messageText = res.data.message
					this.$refs.message.open()
					this.isloading = false
				}
			}).catch(err => {
				this.msg.msgType = "error"
				this.msg.messageText = err.errMsg
				this.$refs.message.open()
				this.isloading = false
			})
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
	}
</style>
