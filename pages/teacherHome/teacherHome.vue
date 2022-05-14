<template>
	<view class="teacher-home">
		<view class="backgrond-style">
		</view>
		<view class="teacher-message">
			<uni-card :title="identity[teacherMessage.role]" :sub-title="teacherMessage.username"
				:extra="teacherMessage.phoneNumber">
				<view slot="actions" class="card-actions">
					<view class="card-actions-item" @click="revisePassword">
						<uni-icons type="loop" size="20" color="#999"></uni-icons>
						<text class="card-actions-item-text">修改密码</text>
					</view>
				</view>
			</uni-card>
		</view>
		<view>
			<!-- 修改密码弹窗 -->
			<uni-popup ref="popupRevisePassword" background-color="#fff" type="bottom">
				<view class="revise-popup-content">
					<uni-forms class="revise-form" ref="revisePasswordForm" :rules="reviseRules"
						:modelValue="reviseFormData" validateTrigger="submit">
						<uni-forms-item name="oldPassword">
							<uni-easyinput type="password" prefixIcon=".uniui-person-filled"
								v-model="reviseFormData.oldPassword" placeholder="请输入原密码" />
						</uni-forms-item>
						<uni-forms-item name="newPassword">
							<uni-easyinput type="password" prefixIcon=".uniui-locked-filled"
								v-model="reviseFormData.newPassword" placeholder="请设置新密码" />
						</uni-forms-item>
					</uni-forms>
					<view class="revise-buttons">
						<button type="default" class="cancel-button" @click="cancelSubmit">取消</button>
						<button type="default" class="revise-button" @click="reviseSubmit">确认</button>
					</view>
				</view>
			</uni-popup>
		</view>
		<view class="teacher-tools">
			<uni-card>
				<view>
					<text>常用工具</text>
				</view>
				<view class="tools-box">
					<view class="teacher">
						<view>
							<navigator animation-type="pop-in" animation-duration="300" url="/pages/handleLeave/handleLeave?choose=0"
								class="tools-btn">
								<uni-icons type="mail-open" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
							</navigator>
							<text>审批请假</text>
						</view>
						<view>
							<navigator animation-type="pop-in" animation-duration="300" url="/pages/classList/classList"
								class="tools-btn">
								<uni-icons type="staff" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
							</navigator>
							<text>管理成员</text>
						</view>
						<view>
							<navigator animation-type="pop-in" animation-duration="300" url="../showGradeLeave/showGradeLeave?choose=1"
								class="tools-btn">
								<uni-icons type="tune" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
							</navigator>
							<text>跨级审批</text>
						</view>
					</view>
						<view>
							<navigator animation-type="pop-in" animation-duration="300" url="/pages/classList/classList"
								class="tools-btn">
								<uni-icons type="gear" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
							</navigator>
							<text>下载假条</text>
						</view>
						<view>
							<navigator class="tools-btn" animation-type="pop-in" animation-duration="300"
								url="/pages/classList/classList">
								<uni-icons type="person" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
							</navigator>
							<text>查看班级</text>
						</view>
				</view>
			</uni-card>
		</view>
		<view class="teacher-tools">
			<uni-card>
				<view>
					<text>历史记录</text>
				</view>
				<view class="tools-box">
					<view>
						<navigator class="tools-btn" animation-type="pop-in" animation-duration="300"
							url="../showGradeLeave/showGradeLeave?choose=2">
							<uni-icons type="personadd-filled" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
						</navigator>
						<text>请假记录</text>
					</view>
					<view>
						<navigator class="tools-btn" animation-type="pop-in" animation-duration="300"
							url="../finishRecord/finishRecord">
							<uni-icons type="home-filled" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
						</navigator>
						<text>请假统计</text>
					</view>
					<view>
						<navigator class="tools-btn" animation-type="pop-in" animation-duration="300"
							url="../finishRecord/finishRecord">
							<uni-icons type="checkbox-filled" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
						</navigator>
						<text>销假统计</text>
					</view>
				</view>
			</uni-card>
		</view>
		<view class="teacher-tools">
			<uni-card>
				<view>
					<text>账号管理</text>
				</view>
				<view class="tools-box">
					<view @click="lookMessage">
						<navigator class="tools-btn">
							<uni-icons type="notification-filled" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
						</navigator>
						<text>个人信息</text>
					</view>
					<view @click="signout">
						<navigator class="tools-btn">
							<uni-icons type="paperplane" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
						</navigator>
						<text>退出登录</text>
					</view>
				</view>
			</uni-card>
		</view>
		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msg.msgType" :message="msg.messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
		</view>
		<view>
			<!-- 退出登录弹框 -->
			<uni-popup ref="alertDialog" type="dialog">
				<uni-popup-dialog type="info" cancelText="取消" confirmText="确认" title="退出登录" content="确认离开嘛"
					@confirm="signoutConfirm"></uni-popup-dialog>
			</uni-popup>
		</view>
		<view>
			<!-- 普通弹窗 -->
			<uni-popup ref="peopleMessagePopup" background-color="#fff" type="bottom">
				<view class="popup-content">
					<uni-list>
						<uni-list-item title="姓名" :rightText="teacherMessage.username"></uni-list-item>
						<uni-list-item title="工号" :rightText="'' + teacherMessage.studentNumber"></uni-list-item>
						<uni-list-item title="性别" :rightText="teacherMessage.sex == 'WOMAN'? '女' : '男'"></uni-list-item>
						<uni-list-item title="联系方式" :rightText="teacherMessage.phoneNumber"></uni-list-item>
					</uni-list>
				</view>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				msg: {
					msgType: 'success',
					messageText: '这是一条成功提示',
				},
				teacherMessage: {},
				reviseFormData: {
					oldPassword: "",
					newPassword: ""
				},
				identity: {
					"INSTRUCTOR": "班主任",
					"SECRETARY": "副党委书记",
					"DEAN": "院长"
				},
				reviseRules: {
					oldPassword: {
						rules: [{
							validateFunction: function(rule, value, data, callback) {
								if (value.length < 6) {
									callback('请输入至少6位数的密码')
								}
								return true
							}
						}]
					},
					newPassword: {
						rules: [{
							validateFunction: function(rule, value, data, callback) {
								if (value == data.oldPassword) {
									callback('新密码与原密码一致')
								}
								if (value.length < 6) {
									callback('请输入至少6位数的密码')
								}
								return true
							}
						}]
					}
				}
			}
		},
		onLoad() {
			// uni.request({
			//     url: 'http://101.43.85.67:8081/user/personInfo', //仅为示例，并非真实接口地址。
			//     header: {
			//         'token': uni.getStorageSync('token') //自定义请求头信息
			//     },
			//     success: (res) => {
			//         console.log(res.data);
			//         this.text = 'request success';
			//     }
			// });
			uni.$http.get("/user/personInfo").then(res => {
				if (res.data.code == 200) {
					this.teacherMessage = res.data.data
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
			this.$nextTick(() => {
				console.log(this.$refs)
			})
		},
		methods: {
			revisePassword() {
				this.$refs.popupRevisePassword.open()
			},
			cancelSubmit() {
				this.$refs.popupRevisePassword.close()
			},
			reviseSubmit() {
				this.$refs.revisePasswordForm.validate().then(res => {
					uni.$http.post("/user/updatePWD", res).then(res => {
						if (res.data.code == 200) {
							uni.showToast({
								title: "正在修改中"
							})
							setTimeout(() => {
								uni.showToast({
									title: "修改成功"
								})
								this.$refs.popupRevisePassword.close()
								uni.navigateTo({
									url: "../login/login"
								})
							}, 1000)
						} else {
							this.msg.msgType = "error"
							this.msg.messageText = res.data.message
							this.$refs.message.open()
							setTimeout(() => {
								this.$refs.popupRevisePassword.close()
							}, 500)
						}
					}).catch(err => {
						this.msg.msgType = "error"
						this.msg.messageText = err.errMsg
						this.$refs.message.open()
						setTimeout(() => {
							this.$refs.popupRevisePassword.close()
						}, 500)
					})
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err.errMsg
					this.$refs.message.open()
				})
			},
			signout() {
				this.$refs.alertDialog.open()
			},
			signoutConfirm() {
				uni.$http.get("/user/logout").then(res => {
					if (res.data.code == 200) {
						uni.showToast({
							title: "退出登录中..."
						})
						uni.removeStorage({
							key: 'token',
						});
						uni.removeStorage({
							key: 'role',
						});
						setTimeout(() => {
							uni.showToast({
								title: "退出成功"
							})
							uni.navigateTo({
								url: "../index/index"
							})
						}, 1000)
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
					}
					this.$refs.alertDialog.close()
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err
					this.$refs.message.open()
					setTimeout(() => {
						this.$refs.alertDialog.close()
					}, 500)
				})
			},
			lookMessage(){
				this.$refs.peopleMessagePopup.open()
			}
		}
	}
</script>

<style lang="scss">
	@mixin bgstyle($bgstyle: #1b478e) {
		width: 100vw;
		height: 310rpx;
		position: absolute;
		top: -100rpx;
		border-radius: 0 0 40rpx 40rpx;
		background: $bgstyle;
	}

	.backgrond-style {
		@include bgstyle;
	}

	.teacher-home {
		.card-actions-item {
			display: flex;
			justify-content: flex-end;
		}
	}

	.revise-form {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-top: 50rpx;

		.uni-easyinput {
			width: 650rpx;
			border-radius: 100rpx;
		}
	}

	.revise-popup-content {
		.revise-buttons {
			display: flex;
			justify-content: center;
			padding-bottom: 50rpx;

			button {
				width: 300rpx;
				height: 60rpx;
				line-height: 60rpx;
				font-size: $jxnu-font-14;
			}

			.revise-button {
				background-color: $jxnu-bg-color;
			}
		}
	}

	.teacher-tools {
		.tools-box {
			display: flex;
			flex-wrap: wrap;
			justify-content: flex-start;
			text-align: center;
			.teacher,.other{
				display: flex;
			}
			.tools-btn {
				padding: 20rpx;
				margin: 20rpx;
				background-color: $jxnu-bg-color;
				color: $uni-text-color;
				border-radius: 30rpx;
			}
		}
	}
</style>
