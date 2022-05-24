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
				loginFormData: {
					"studentNumber": "",
					"password": ""
				},
				studentRules: {
					"studentNumber": {
						rules: [{
							required: true,
							errorMessage: "请输入正确的学号"
						},{
							format: 'number',
							errorMessage: '学号只能输入数字'
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
						},{
							format: 'number',
							errorMessage: '工号只能输入数字'
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
			if (uni.getStorageSync('token') && uni.getStorageSync('role') != '学生' && option.card == "teacher") {
				//console.log(uni.getStorageSync('teacherMessage'))
				let message = uni.getStorageSync('teacherMessage')
				this.loginFormData.studentNumber = message.studentNumber
				this.loginFormData.password = message.password
				uni.removeStorageSync('token');
				uni.removeStorageSync('role');
				uni.removeStorageSync('teacherMessage');
			} else if (uni.getStorageSync('token') && uni.getStorageSync('role') == '学生' && option.card == "student") {
				let message = uni.getStorageSync('studentMessage')
				this.loginFormData.studentNumber = message.studentNumber
				this.loginFormData.password = message.password
				uni.removeStorageSync('token');
				uni.removeStorageSync('role');
				uni.removeStorageSync('studentMessage');
			}
			this.optionChoose = option.card == "student" ? true : false
			// this.cardChoose(option.card)
		},
		computed: {},
		methods: {
			cardChoose(card) {
				if (card == "student") {
					this.loginFormData = {
						"studentNumber": "202026202036",
						"password": "17309480686"
					}
				} else {
					this.loginFormData = {
						"studentNumber": "113062",
						"password": "13170918684"

					}
				}
			},
			loginSubmit(ref) {
				//console.log(ref)
				this.$refs[ref].validate().then(res => {
					if (ref === "teacherForm") {
						uni.$http.post("/user/login", this.loginFormData).then(res => {
							if (res.data.code == 200) {
								if(res.data.data.role !== "学生"){
									uni.showToast({
										title: "登录成功，请稍等.."
									})
									uni.setStorage({
										key: 'token',
										data: res.data.data.token,
									});
									console.log(res.data.data.role)
									uni.setStorage({
										key: 'role',
										data: res.data.data.role,
									});
									uni.setStorage({
										key: 'teacherMessage',
										data: this.loginFormData
									});
									uni.redirectTo({
										url: '/pages/teacherHome/teacherHome'
									})
								}else{
									uni.showToast({
										icon:"none",
										title:"身份验证失败,请从学生端登录"
									})
								}
							} else {
								this.msg.msgType = "error"
								this.msg.messageText = res.data.message
								this.$refs.message.open()
							}
						})
					} else if (ref === "studentForm") {
						uni.$http.post("/user/login", this.loginFormData).then(res => {
							if (res.data.code == 200) {
								if(res.data.data.role == "学生"){
										uni.setStorage({
											key: 'token',
											data: res.data.data.token,
											// success: function () {
											// 	console.log('success');
											// }
										});
										uni.setStorage({
											key: 'role',
											data: res.data.data.role,
										});
										uni.setStorage({
											key: 'studentMessage',
											data: this.loginFormData
										});
										uni.redirectTo({
											url: '/pages/studentHome/studentHome'
										})
									}else{
										uni.showToast({
											icon:"none",
											title:"身份验证失败,请从教师端登录"
										})
									} 
								}else {
									uni.showToast({
										icon:"none",
										title: res.data.message
									})
								}	
						})
					}
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
