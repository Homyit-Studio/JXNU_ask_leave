<template>
	<view class="handle-leave-details-page">
		<view class="detail-status">
			<uni-tag v-if="leaveDetails.examine == 'SUCCESS'" text="已同意" type="success"></uni-tag>
			<uni-tag v-else-if="leaveDetails.examine == 'FAILURE'" text="已拒绝" type="error"></uni-tag>
			<uni-tag v-else text="审核中" type="primary"></uni-tag>
			<view class="current-time">
				<text class="time-tag">当前时间：2020-12-22 20:11:11</text>
			</view>
		</view>
		<view class="details-card">
			<uni-card :title="leaveDetails.username + ' 的请假申请'">
				<view><text decode="true">学&emsp;&emsp;号:&emsp;{{leaveDetails.studentNumber}}</text></view>
				<view><text decode="true">班&emsp;&emsp;级:&emsp;{{leaveDetails.majorAndClass}}</text></view>
				<view><text decode="true">目的地点:&emsp;{{leaveDetails.destination}}</text></view>
				<view><text decode="true">请假原因:&emsp;{{leaveDetails.reason}}</text></view>
				<view><text decode="true">是否离校:&emsp;{{leaveDetails.depart == 'YES'? '是' :'否'}}</text></view>
				<view class="time-box">
					<view><text decode="true">开始时间:&emsp;{{leaveDetails.startTime}}</text></view>
					<view><text decode="true">结束时间:&emsp;{{leaveDetails.endTime}}</text></view>
					<view class="time-tag"><text>{{leaveDetails.days}}</text></view>
				</view>
				<view><text decode="true">前往方式:&emsp;{{leaveDetails.way}}</text></view>
				<view><text decode="true">联系号码:&emsp;{{leaveDetails.phoneNumber}}</text></view>
				<view><text decode="true">宿舍地址:&emsp;{{leaveDetails.dormitoryNumber}}</text></view>
			</uni-card>
		</view>
		<view class="approval-record">
			<view class="record">审批流程记录</view>
			<view>
				<uni-steps :options="process" :active="activeProcess" direction="column">
				</uni-steps>
			</view>
		</view>
		<view class="handle-buttons" v-if="currentStatus == 'NO'">
			<button type="warn" class="refuse-button" @click="cancelSubmit">拒绝</button>
			<button type="primary" class="agree-button" @click="reviseSubmit">同意</button>
		</view>
		<view>
			<!-- 输入框示例 -->
			<uni-popup ref="inputDialog" type="dialog">
				<uni-popup-dialog ref="inputClose" mode="input" title="审核意见" value="对话框预置提示内容!" placeholder="请输入审核意见"
					@confirm="dialogInputConfirm"></uni-popup-dialog>
			</uni-popup>
		</view>
		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				leaveDetails: {},
				process: [],
				msg: {
					msgType: 'success',
					messageText: '这是一条成功提示',
				},
				activeProcess: null,
				opinionEnum: null,
				currentStatus: null
			}
		},
		onLoad(options) {
			console.log(options)
			this.showLeaveDetail(options.id, options.current)
		},
		methods: {
			showLeaveDetail(id, current) {
				this.currentStatus = current
				console.log(this.currentStatus)
				uni.$http.get(`/leave/selectANote/${id}`).then(res => {
					if (res.data.code == 200) {
						uni.showToast({
							title: '加载中',
							duration: 1000,
							icon: "loading"
						});
						this.leaveDetails = res.data.data
						//审核进程
						this.pushProcess(res.data.data)
						let processIn = this.process.some(element => {
							return res.data.data.examine == element.other
						})
						if (processIn) {
							for (let key in this.process) {
								console.log(this.process[key])
								if (current == "NO") {
									if (this.process[key].other == res.data.data.examine) {
										this.activeProcess = key - 1
										console.log(this.activeProcess, this.process[key].other, res.data.data
											.examine)
										break
									}
								} else if (current == "YES") {
									if (this.process[key].other == res.data.data.examine) {
										this.activeProcess = key - 1
										break
									}
								}
							}
						} else {
							this.activeProcess = this.process.length - 1
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
					}
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err
					this.$refs.message.open()
				})
			},
			pushProcess(data) {
				if (data.level == "INSTRUCTOR") {
					this.process = [{
							title: "学生发起申请",
							desc: data.startTime,
							other: "student"
						},
						{
							title: "辅导员审批",
							desc: data.instructorOpinion,
							other: "INSTRUCTOR"
						}
					]
				} else if (data.level == "SECRETARY") {
					this.process = [{
							title: "学生发起申请",
							desc: data.startTime,
							other: "sutdent"
						},
						{
							title: "辅导员审批",
							desc: data.instructorOpinion,
							other: "INSTRUCTOR"
						},
						{
							title: "党委书记审批",
							desc: data.instituteOpinion,
							other: "SECRETARY"
						}
					]
				} else if (data.level == "DEAN") {
					this.process = [{
							title: "学生发起申请",
							desc: data.startTime,
							other: "student"
						},
						{
							title: "辅导员审批",
							desc: data.instructorOpinion,
							other: "INSTRUCTOR"
						},
						{
							title: "党委书记审批",
							desc: data.instituteOpinion,
							other: "SECRETARY"
						},
						{
							title: "院长审批",
							desc: data.deanOpinion,
							other: "DEAN"
						}
					]
				}
			},
			//拒绝申请
			cancelSubmit() {
				this.$refs.inputDialog.open()
				this.opinionEnum = "NO"
			},
			//同意申请
			reviseSubmit() {
				this.$refs.inputDialog.open()
				this.opinionEnum = "YES"
			},
			//确认审核提交
			dialogInputConfirm(val) {
				uni.showLoading({
					title: '审核提交中...'
				})

				uni.$http.post("/leave/updateNote", {
					"id": this.leaveDetails.id,
					"examineEnum": this.leaveDetails.examine,
					"levelEnum": this.leaveDetails.level,
					"opinionEnum": this.opinionEnum,
					"instructorOpinion": val
				}).then(res => {
					console.log(res)
					if (res.data.code == 200) {
						uni.hideLoading()
						this.msg.msgType = "success"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
						uni.navigateTo({
							url: "../handleLeave/handleLeave"
						})
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
					}
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err
					this.$refs.message.open()
				})
			},
		}
	}
</script>

<style lang="scss">
	.handle-leave-details-page {
		background-color: #f8f8f8;

		.detail-status {
			text-align: center;
			background-color: #fff;
			color: #378d8c;
			padding: 10rpx 0;
		}

		.current-time {
			font-size: 12rpx;
			text-align: center;
			padding-top: 20rpx;

			.time-tag {
				padding: 5rpx;
				background-color: #999;
				border-radius: 10rpx;
				color: #fff;
			}
		}

		.details-card {
			.uni-card {
				margin-top: 0;
				padding-top: 0;

				.time-box {
					position: relative;

					.time-tag {
						position: absolute;
						right: 0;
						top: 20rpx;
						padding: 20rpx;
						color: #fff;
						background-color: #1b478e;
					}
				}
			}
		}

		.approval-record {
			background-color: #fff;

			padding: 20rpx 70rpx;
		}

		.handle-buttons {
			display: flex;
			justify-content: center;
			padding: 50rpx 0;

			button {
				width: 300rpx;
				height: 60rpx;
				line-height: 60rpx;
				font-size: $jxnu-font-14;
			}

			.agree-button {
				background-color: $jxnu-bg-color;
			}
		}
	}
</style>
