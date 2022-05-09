<template>
	<view class="grade-content">
		<view class="grade-select">
			<uni-data-select v-model="gradevalue" :localdata="gradeSelect" @change="changeGrade" label="年级">
			</uni-data-select>
		</view>
		<view class="handle-leave">
			<view class="uni-padding-wrap uni-common-mt">
				<uni-segmented-control :current="currentIndex" :values="handleChoices" style-type="text"
					active-color="#1b478e" @clickItem="onClickChoice" />
			</view>
			<view class="leave-notes">
				<uni-card v-for="(item, index) in leaveNoteList" :key="item.id">
					<template v-slot:title>
						<view class="card-header">
							<uni-tag v-if="item.examine == 'SUCCESS'" text="已同意" type="success"></uni-tag>
							<uni-tag v-else-if="item.examine == 'FAILURE'" text="已拒绝" type="error"></uni-tag>
							<uni-tag v-else text="审核中" type="primary"></uni-tag>
							<view>
								<text>{{item.startTime}}</text>
							</view>
						</view>
					</template>
					<view><text decode="true">姓&emsp;&emsp;名: {{item.username}}</text></view>
					<view><text decode="true">班&emsp;&emsp;级: {{item.majorAndClass}}</text></view>
					<view><text decode="true">学&emsp;&emsp;号: {{item.studentNumber}}</text></view>
					<view><text>是否离校: {{item.depart == 'YES'? '是' :'否'}}</text></view>
					<view><text>请假时长: {{item.days}}</text></view>
					<view slot="actions" class="card-actions">
						<view class="card-actions-item" @click="checkDetails(item.id)">
							<text class="card-actions-item-text">查看详情</text>
							<uni-icons type="arrow-right" size="20" color="#1b478e"></uni-icons>
						</view>
					</view>
				</uni-card>
			</view>
			<view v-if="shownodata">
				<view class="show-nodata"><text>没有更多数据了</text></view>
			</view>
			<view>
				<!-- 提示信息弹窗 -->
				<uni-popup ref="message" type="message">
					<uni-popup-message :type="msg.msgType" :message="msg.messageText" :duration="2000">
					</uni-popup-message>
				</uni-popup>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
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
					"completeEnum": "NO",
					"gradeNumber": "2021"
				}
			}
		},
		onLoad() {
			this.requestLeaveNotes()
		},
		methods: {
			changeGrade(grade){
				this.listRequest.gradeNumber = grade
				this.listRequest.pageNo = 1
				this.requestLeaveNotes()
			},
			onClickChoice(index) {
				if (index.currentIndex == 0) {
					this.listRequest.completeEnum = "NO"
					this.listRequest.pageNo = 1
					this.shownodata = false
					this.requestLeaveNotes()
				} else {
					this.listRequest.pageNo = 1
					this.listRequest.completeEnum = "YES"
					this.shownodata = false
					this.requestLeaveNotes()
				}
			},
			requestLeaveNotes() {
				uni.$http.post("/leave/selectNodeByGrade", this.listRequest).then(res => {
					if (res.data.code == 200) {
						uni.showToast({
							title: '加载中',
							duration: 1000,
							icon: "loading"
						});
						this.leaveNoteList = res.data.data.list
						this.endPage = res.data.data.endPage
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
					url: `../handleLeaveDetail/handleLeaveDetail?id=${id}&current=${this.listRequest.completeEnum}&card=other`,
					animationType: 'pop-in',
					animationDuration: 200
				})
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

		.handle-leave {
			margin: 0 10rpx;

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
		.show-nodata{
			text-align: center;
			padding: 20px;
		}
	}
</style>
