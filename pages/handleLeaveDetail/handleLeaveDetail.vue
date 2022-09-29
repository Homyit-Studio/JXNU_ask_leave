<template>
	<view class="handle-leave-details-page">
		<view class="detail-status">
			<!-- <uni-tag v-if="leaveDetails.examine == 'SUCCESS'" text="已同意" type="success"></uni-tag>
			<uni-tag v-else-if="leaveDetails.examine == 'FAILURE'" text="已拒绝" type="error"></uni-tag>
			<uni-tag v-else text="审核中" type="primary"></uni-tag> -->
			<text>{{currentProcess}}</text>
			<!-- 			<view class="current-time">
				<text class="time-tag">当前时间：2020-12-22 20:11:11</text>
			</view> -->
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
				<view><text decode="true">宿舍住址:&emsp;{{leaveDetails.dormitoryNumber}}</text></view>
				<view class="cofirm-image"><text decode="true">请假凭证:&emsp;</text>
					<!-- <text>未上传凭证</text> -->
					<text v-if="confirmImg == null">学生未上传请假凭证</text>
					<view v-else>
						<image v-for="item in confirmImg" :key="item.id" :src="item.url" mode="aspectFill"
							@click="preview(item)"></image>
					</view>
				</view>
			</uni-card>
		</view>
		<view class="approval-record">
			<view class="record">审批流程记录</view>
			<view>
				<uni-steps :options="process" :active="activeProcess" direction="column">
				</uni-steps>
			</view>
		</view>
		<view class="handle-buttons"
			v-if="(statusCard == 3 && currentStatus == 'PROCESSING') || ((identity == leaveDetails.level || leaveDetails.level == 'INSTRUCTOR' || leaveDetails.examine == 'INSTRUCTOR')&&statusCard == 1 && currentStatus == 'PROCESSING')">
			<button type="warn" class="refuse-button" @click="cancelSubmit">拒绝</button>
			<button type="primary" class="agree-button" @click="reviseSubmit">同意</button>
		</view>
		<view>
			<!-- 弹框 -->
			<uni-popup ref="inputDialog" type="dialog">
				<template v-slot:default>
					<view class="confirm-dialog">
						<uni-forms :modelValue="processMessage" ref="reviceMessage" label-position="top"
							label-width="90" :rules="commitRules">
							<uni-forms-item label="选择负责人" name="checkpeople" required
								v-if="agree && identity == 'INSTRUCTOR' && leaveDetails.level != 'INSTRUCTOR'">
								<uni-data-checkbox multiple v-model="processMessage.checkpeople"
									:localdata="nextPeople" />
							</uni-forms-item>

							<uni-forms-item name="advice" label="审批建议">
								<uni-easyinput type="textarea" v-model="processMessage.teacherOpinion"
									placeholder="请输入审核意见"/>
							</uni-forms-item>
						</uni-forms>
						<view class="confirm-button">
							<button plain size="mini" @click="cancelConfirm('reviceMessage')">取消</button>
							<button plain size="mini" @click="dialogInputConfirm('reviceMessage')">确认</button>
						</view>
					</view>
				</template>
			</uni-popup>
		</view>
		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msg.msgType" :message="msg.messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
		</view>
		<view
			v-if="leaveDetails.examine == 'WAIT_REPORT' || leaveDetails.examine == 'PROCESSED' || leaveDetails.examine == 'WAIT_REPORT'">
			<uni-card title="销假记录">
				<view v-if="leaveDetails.examine== 'PROCESSED'">
					<view><text decode="true">是否离校:&emsp;{{gobackMessage.depart == 'YES'? '是' :'否'}}</text></view>
					<view><text decode="true">离开时间:&emsp;{{gobackMessage.departTime}}</text></view>
					<view><text decode="true">离开方式:&emsp;{{gobackMessage.departWay}}</text></view>
					<view><text decode="true">是否返校:&emsp;{{gobackMessage.back == 'YES'? '是' :'否'}}</text></view>
					<view><text decode="true">返回时间:&emsp;{{gobackMessage.backTime}}</text></view>
					<view><text decode="true">返回方式:&emsp;{{gobackMessage.backWay}}</text></view>
				</view>
				<view v-else-if="leaveDetails.examine == 'WAIT_REPORT'">
					<view><text>待学生销假中</text></view>
				</view>
				<view v-else-if="leaveDetails.examine == 'REPORT_EXPIRED'">
					<view><text>学生在请假期限内未进行销假</text></view>
				</view>
			</uni-card>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				confirmImg: null,
				agree: null,
				leaveDetails: {},
				gobackMessage: {},
				process: [],
				processMessage: {
					teacherOpinion: "",
					checkpeople: [],
				},
				nextPeople: [],
				statusCard: null,
				commitRules: {
					"checkpeople": {
						rules: [{
							required: true,
							errorMessage: "负责人不能为空"
						}]
					}
				},
				msg: {
					msgType: 'success',
					messageText: '这是一条成功提示',
				},
				activeProcess: null,
				opinionEnum: null,
				currentStatus: null,
				identity: null
			}
		},
		computed: {
			currentProcess: {
				get() {
					// console.log(this.leaveDetails.examine)
					if (this.leaveDetails.examine == 'PROCESSING') {
						return '等待审核中'
					} else if (this.leaveDetails.examine == 'APPLY_EXPIRED') {
						return '申请已过期'
					} else if (this.leaveDetails.examine == 'TRANSMIT') {
						return "上级审核中"
					} else if (this.leaveDetails.examine == 'WAIT_REPORT') {
						return "待学生销假"
					} else if (this.leaveDetails.examine == 'REPORT_EXPIRED') {
						return "学生未销假，销假过期"
					} else if (this.leaveDetails.examine == 'PROCESSED') {
						return "学生已完成销假"
					} else if (this.leaveDetails.examine == 'FAILURE') {
						return "被拒假条"
					} else {
						return '等待审核中'
					}
				}
			}
		},
		onLoad(options) {
			this.showLeaveDetail(options.id)
			this.statusCard = options.card
			this.currentStatus = options.current
			this.getLeaders()
		},
		onReady() {
			this.identity = uni.getStorageSync('identity')
		},
		methods: {
			showLeaveDetail(id) {
				uni.showLoading({
					title:"正在获取请假信息"
				})
				uni.$http.get(`/leave/selectANote/${id}`).then(res => {
					if (res.data.code == 200) {
						this.leaveDetails = res.data.data
						//审核进程
						uni.hideLoading()
						this.pushProcess(res.data.data)
						let processIn = this.process.some(element => {
							return res.data.data.examine == element.other
						})
						if (processIn) {
							for (let key in this.process) {
								if (this.process[key].other == res.data.data.examine) {
									this.activeProcess = key
									break
								}
							}
						} else {
							if (res.data.data.examine == 'APPLY_EXPIRED' || res.data.data.examine == 'FAILURE') {
								if (res.data.data.instructorOpinion != null && res.data.data.secretaryOpinion ==
									null && res.data.data.secretaryOpinion == null) {
									this.activeProcess = 1
								} else if (res.data.data.instructorOpinion != null && res.data.data
									.secretaryOpinion != null && res.data.data.secretaryOpinion == null) {
									this.activeProcess = 2
								} else if (res.data.data.instructorOpinion != null && res.data.data
									.secretaryOpinion != null && res.data.data.secretaryOpinion != null) {
									this.activeProcess = 3
								} else {
									this.activeProcess = 0
								}
							} else {
								this.activeProcess = this.process.length - 1
							}
						}
						//获取销假凭证
						uni.$http.get(`/image/${id}`).then(res => {
							if (res.data.code == 200) {
								// console.log(res.data.data)
								this.confirmImg = res.data.data
								for (let item in this.confirmImg) {
									this.confirmImg[item].url = "https://leave.jxnu.edu.cn" + this.confirmImg[item].url;
								}
							} else {
								this.confirmImg = null
							}
						})
						//销假申请
						if (this.leaveDetails.examine == 'PROCESSED') {
							uni.$http.get(`/back/selectANote/${id}`).then(res => {
								if (res.data.code == 200) {
									
									this.gobackMessage = res.data.data
								} else {
									this.msg.msgType = "error"
									this.msg.messageText = "请求销假错误"
									this.$refs.message.open()
								}
							})
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = "请求假条错误"
						this.$refs.message.open()
					}
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
							title: "班主任审批",
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
							title: "班主任审批",
							desc: data.instructorOpinion,
							other: "INSTRUCTOR"
						},
						{
							title: "负责人审批",
							desc: data.secretaryOpinion,
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
							title: "班主任审批",
							desc: data.instructorOpinion,
							other: "INSTRUCTOR"
						},
						{
							title: "负责人审批",
							desc: data.secretaryOpinion,
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
				this.agree = false
				this.$refs.inputDialog.open()
				this.opinionEnum = "NO"
			},
			//同意申请
			reviseSubmit() {
				this.agree = true;
				this.opinionEnum = "YES"
				this.getLeaders()
				this.$refs.inputDialog.open()
			},
			//获取负责人
			getLeaders() {
				if (this.nextPeople.length == 0) {
					uni.$http.get("/user/getAllLeaders").then(res => {
						if (res.data.code == 200) {
							for (let item in res.data.data) {
								let peopleobj = {}
								peopleobj.text = item;
								peopleobj.value = res.data.data[item]
								this.nextPeople.push(peopleobj)
								// this.$refs.inputDialog.open()
							}
						} else {
							this.msg.msgType = "error"
							this.msg.messageText = "请求负责人错误"
							this.$refs.message.open()
						}
					})
				}
			},
			//图片预览
			preview(e) {
				//console.log(e)
				let array = [];
				array.push(e.url);
				uni.previewImage({
					current: array[0],
					urls: array
				});
			},
			cancelConfirm() {
				this.$refs.inputDialog.close()
			},
			//确认审核提交
			dialogInputConfirm(ref) {
				this.$refs[ref].validate().then(res => {
					let status = this.identity.toLowerCase() + "Opinion"
					let requestMessage = {
						"id": this.leaveDetails.id,
						"levelEnum": this.leaveDetails.level,
						"opinionEnum": this.opinionEnum
					}
					if(this.opinionEnum == 'YES' && this.processMessage.teacherOpinion == ""){
						console.log("空")
						this.processMessage.teacherOpinion = "同意"
					}else if(this.opinionEnum == 'NO' && this.processMessage.teacherOpinion == ""){
						this.processMessage.teacherOpinion = "拒绝"
					}
					requestMessage[status] = this.processMessage.teacherOpinion
					if (this.opinionEnum == 'YES') {
						requestMessage['leaderNumber'] = this.processMessage.checkpeople.join()
					}
					this.$refs.inputDialog.close()
					// console.log(requestMessage)
					uni.$http.post("/leave/updateNote", requestMessage).then(res => {
						// console.log(res)
						if (res.data.code == 200) {
							this.msg.msgType = "success"
							this.msg.messageText = "审批成功"
							setTimeout(() => {
								uni.showToast({
									title:"审批成功",
									icon:"success"
								})
							}, 100)
							setTimeout(() => {
								uni.navigateBack({})
							}, 2000)
						} else {
							this.msg.msgType = "error"
							this.msg.messageText = res.data.message
							this.$refs.message.open()
						}
					})
				})
			}
		}
	}
</script>

<style lang="scss">
	.handle-leave-details-page {
		background-color: #f8f8f8;

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
				justify-content: space-around;
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

		.detail-status {
			text-align: center;
			background-color: $jxnu-bg-color;
			color: #fff;
			padding: 20rpx 0;
			font-size: 14px;
		}


		.cofirm-image {
			cover-view {
				width: 600rpx;
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
						top: 12rpx;
						font-size: 12px;
						padding: 10rpx;
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
