<template>
	<view class="login-content">
		<view>
			<image class="login-img" src="https://uis.jxnu.edu.cn/cas/file/png/logoM.png" lazy-load mode="aspectFit"
				fade-show></image>
		</view>
		<view class="login-card">
			<text class="uni-h6">账号登录</text>
		</view>
		<view class="login-form" v-if="optionChoose">
			<uni-forms ref="studentForm" :rules="studentRules" :modelValue="loginFormData" validateTrigger="submit">
				<uni-forms-item name="studentNumber">
					<uni-easyinput type="text" prefixIcon=".uniui-person-filled" v-model="loginFormData.studentNumber"
						placeholder="请输入工号或学号" />
				</uni-forms-item>
				<uni-forms-item name="password">
					<uni-easyinput type="password" prefixIcon=".uniui-locked-filled" v-model="loginFormData.password"
						placeholder="请输入登录密码" />
				</uni-forms-item>
			</uni-forms>
			<button type="default" class="button" @click="loginSubmit('studentForm')">提交</button>
		</view>
		<view class="login-form" v-else>
			<uni-forms ref="teacherForm" :rules="teacherRules" :modelValue="loginFormData">
				<uni-forms-item name="studentNumber">
					<uni-easyinput type="text" prefixIcon=".uniui-person-filled" v-model="loginFormData.studentNumber"
						placeholder="请输入工号或学号" />
				</uni-forms-item>
				<uni-forms-item name="password">
					<uni-easyinput type="password" prefixIcon=".uniui-locked-filled" v-model="loginFormData.password"
						placeholder="请输入登录密码" />
				</uni-forms-item>
			</uni-forms>
			<button type="default" class="button" @click="loginSubmit('teacherForm')">提交</button>
		</view>
		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msg.msgType" :message="msg.messageText" :duration="2000"></uni-popup-message>
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
				loginFormData: {},
				studentRules: {
					"studentNumber": {
						rules: [{
							required: true,
							errorMessage: "请输入正确的学号"
						}]
					},
					"password": {
						rules: [{
							required: true,
							errorMessage: "请输入正确的密码"
						}]
					}
				},
				teacherRules: {
					"studentNumber": {
						rules: [{
							required: true,
							errorMessage: "请输入正确的工号"
						}]
					},
					"password": {
						rules: [{
							required: true,
							errorMessage: "请输入正确的密码"
						}]
					}
				},
				optionChoose: ""
			}
		},
		onLoad(option) {
			this.optionChoose = option.card == "student" ? true : false
			this.cardChoose(option.card)
		},
		computed: {},
		methods: {
			cardChoose(card) {
				if (card == "student") {
					this.loginFormData = {
						"studentNumber": null,
						"password": ""
					}
				} else {
					this.loginFormData = {
						"studentNumber": "102126204062",
						"password": "204062"
					}
				}
			},
			loginSubmit(ref) {
				console.log(ref)
				this.$refs[ref].validate().then(res => {
					setTimeout(() => {
						if (ref === "studentForm") {
							uni.navigateTo({
								url: '/pages/studentHome/studentHome'
							})
						}
					}, 1000);
					if (ref === "teacherForm") {
						uni.$http.post("/user/login", this.loginFormData).then(res => {
							if (res.data.code == 200) {
								uni.showToast({
									title:"登录成功，请稍等.."
								})
								uni.setStorage({
									key: 'token',
									data: res.data.data.token,
									success: function() {
										console.log('success');
									}
								});
								uni.navigateTo({
									url: '/pages/teacherHome/teacherHome'
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
					}
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err
					this.$refs.message.open()
				})
			}
		}
	}
</script>

<style lang="scss">
	.login-content {
		width: 700rpx;
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		margin: 0 auto;

		.login-img {
			width: 500rpx;
			height: 200rpx;
		}

		.login-card {
			color: $jxnu-bg-color;
			text-align: center;
			width: 700rpx;
			margin-bottom: 40rpx;
			font-size: $jxnu-font-14;
		}
	}

	.login-form {
		.uni-easyinput {
			width: 650rpx;
			border-radius: 100rpx;
		}

		.button {
			margin-top: 50rpx;
			background-color: $jxnu-bg-color;
			color: $uni-text-color-inverse;
			border-radius: 100rpx;
			font-size: $jxnu-font-14;
		}
	}
</style>
