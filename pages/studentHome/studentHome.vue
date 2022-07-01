<template>
	<view class="teacher-home">
		<view class="backgrond-style">
		</view>
		<view class="teacher-message">
			<uni-card :title="studentMessage.username" :extra="studentMessage.studentNumber + ''" sub-title="学生">
				<view slot="actions" class="card-actions">
					<view class="card-actions-item" @click="revisePassword">
						<uni-icons type="loop" size="20" color="#999"></uni-icons>
						<text class="card-actions-item-text">修改密码</text>
					</view>
				</view>
			</uni-card>
		</view>
		<view class="teacher-tools">
			<uni-card>
				<view>
					<text>常用工具</text>
				</view>
				<view class="tools-box">
					<view>
						<view class="tools-btn" @click="applyLeave()">
							<image src="../../static/stu_icon/woyaoqingjia.png" mode="" class="tools-img"></image>
						</view>
						<text>请假申请</text>
					</view>
					<view>
						<view class="tools-btn" @click="allLeaves()">
							<image src="../../static/stu_icon/qingjiajilu.png" mode="" class="tools-img"></image>
						</view>
						<text>申请记录</text>
					</view>
					<view class="tools-box">
						<view>
							<view class="tools-btn" @click="goTerminate()">
								<image src="../../static/stu_icon/qingjiaxiaojia.png" mode="" class="tools-img"></image>
							</view>
							<text>假条销假</text>
						</view>
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
					<view>
						<view class="tools-btn" @click="showInfo()">
							<image src="../../static/stu_icon/wodegerenxinxi.png" mode="" class="tools-img"></image>
						</view>
						<text>个人信息</text>
					</view>
					<view>
						<view class="tools-btn" @click="logOut()">
							<image src="../../static/stu_icon/tuichudenglu.png" mode="" class="tools-img"></image>
						</view>
						<text>退出登录</text>
					</view>
					<view>
						<navigator class="tools-btn" url="../contactUs/contactUs">
							<image src="../../static/stu_icon/lianxiwomen.png" mode="" class="tools-img"></image>
						</navigator>
						<text>联系我们</text>
					</view>
				</view>
			</uni-card>
		</view>
		<!-- 确认退出弹框 -->
		<uni-popup ref="dialog_up" type="dialog">
			<uni-popup-dialog type="error" content="确认退出？" :duration="2000" :before-close="true" @close="close"
				@confirm="confirm"></uni-popup-dialog>
		</uni-popup>
		<!-- 个人信息展示/修改弹窗 -->
		<uni-popup ref="popupupdateUserForStudent" background-color="#fff" type="bottom">
			<view>
				<!-- <uni-section></uni-section> -->
				<view class="list-title">
					<text class="list-title">点击修改个人信息</text>
				</view>
				<uni-list>
					<uni-list-item title="本人手机号" :rightText="studentMessage.phoneNumber" clickable :showArrow="true"
						@click="onClick1()"></uni-list-item>
					<uni-list-item title="家长手机号" :rightText="studentMessage.parentNumber" clickable :showArrow="true"
						@click="onClick2()"></uni-list-item>
					<uni-list-item title="宿舍楼栋" :rightText="studentMessage.buildingNumber" clickable :showArrow="true"
						@click="onClick3()"></uni-list-item>
					<uni-list-item title="宿舍号" :rightText="studentMessage.dormitoryNumber" clickable :showArrow="true"
						@click="onClick4()"></uni-list-item>
				</uni-list>
			</view>
		</uni-popup>
		<!-- 修改密码弹窗 -->
		<uni-popup ref="popupRevisePassword" background-color="#fff" type="bottom">
			<view class="revise-popup-content">
				<uni-forms class="revise-form" ref="revisePasswordForm" :modelValue="reviseFormData" :rules="dataRules"
					validateTrigger="submit">
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
		<!-- 修改手机号 -->
		<uni-popup ref="updateOne" type="dialog">
			<uni-popup-dialog mode="input" title="输入内容" message="成功消息" placeholder="请输入本人手机号" :duration="2000"
				@close="closeOne" @confirm="confirmOne"></uni-popup-dialog>
		</uni-popup>
		<!-- 修改父母手机号 -->
		<uni-popup ref="updateTwo" type="dialog">
			<uni-popup-dialog mode="input" title="输入内容" message="成功消息" placeholder="请输入家长手机号" :duration="2000"
				@close="closeTwo" @confirm="confirmTwo"></uni-popup-dialog>
		</uni-popup>
		<!-- 修改宿舍楼 -->
		<uni-popup ref="updateThree" type="dialog">
			<uni-popup-dialog mode="input" title="输入内容" message="成功消息" placeholder="请输入宿舍楼栋" :duration="2000"
				@close="closeThree" @confirm="confirmThree"></uni-popup-dialog>
		</uni-popup>
		<!-- 修改宿舍号 -->
		<uni-popup ref="updateFour" type="dialog">
			<uni-popup-dialog mode="input" title="输入内容" message="成功消息" placeholder="请输入宿舍号" :duration="2000"
				@close="closeFour" @confirm="confirmFour"></uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				is_update: false,
				studentMessage: {
					studentNumber: "null",
					username: "null",
				},
				reviseFormData: {
					oldPassword: "",
					newPassword: ""
				},
				dataRules: {
					"oldPassword": {
						rules: [{
							required: true,
							errorMessage: "请输入原密码"
						}]
					},
					"newPassword": {
						rules: [{
							required: true,
							errorMessage: "请输入新密码"
						}]
					},
				}
			}
		},
		onLoad() {
			this.getData();
			//console.log(uni.getStorageSync("token"))
		},
		methods: {
			submitUpdate(str) {
				uni.$http.post(
					'/user/updateUserForStudent', {
						"phoneNumber": this.studentMessage.phoneNumber,
						"parentNumber": this.studentMessage.parentNumber,
						"buildingNumber": this.studentMessage.buildingNumber,
						"dormitoryNumber": this.studentMessage.dormitoryNumber
					}
				).then(res => {
					//console.log(res);
					if (res.data.code === 200) {
						uni.showToast({
							icon: "success",
							title: "修改成功"
						})
						switch (str) {
							case "one":
								this.closeOne()
								break;
							case "two":
								this.closeFour()
								break;
							case "three":
								this.closeThree()
								break;
							case "four":
								this.closeFour()
								break;
						}
					}
				}).catch(err => {
					uni.showToast({
						icon: "none",
						title: "网络似乎有问题"
					})
				})
			},
			showInfo() {
				this.$refs.popupupdateUserForStudent.open()
			},
			getData() {
				uni.$http.get('/user/personInfo').then((res) => {
					//console.log(res.data)
					let userData = res.data.data;
					this.studentMessage = userData;

					//储存学生信息，方便其他页面获取
					uni.setStorage({
						key: 'userStr',
						data: JSON.stringify(userData),
						success: function() {
							//console.log('success');
						}
					});
				}).catch((err) => {
					uni.showToast({
						icon: "none",
						title: "网络似乎出现了一些问题",
					})
				})
			},
			//打开修改密码
			revisePassword() {
				this.$refs.popupRevisePassword.open();
			},
			//关闭修改密码
			cancelSubmit() {
				this.$refs.popupRevisePassword.close();
			},
			//提交修改密码
			reviseSubmit() {
				this.$refs.revisePasswordForm.validate().then(res => {
					uni.showToast({
						title: "正在修改中"
					})
					uni.$http.post('/user/updatePWD', {
						"oldPassword": this.reviseFormData.oldPassword,
						"newPassword": this.reviseFormData.newPassword
					}).then(res => {
						console.log(res)
						if (res.data.code === 200) {
							uni.showToast({
								icon: "none",
								title: "修改成功,请重新登录",
							})
							setTimeout(() => {
								this.$refs.popupRevisePassword.close();
								this.confirm(); //相当于退出登录
							}, 1500)
						} else {
							//this.$errShowToast(res.data.message);
							uni.showToast({
								icon: "none",
								title: res.data.message,
							})
						}
					})
				}).catch(err => {
					//this.$errShowToast(err);
					// uni.showToast({
					// 	title: "网络似乎出现了一些问题",
					// })
				})
			},
			//退出登录
			logOut() {
				this.$refs.dialog_up.open()
			},
			//弹框操作
			close() {
				this.$refs.dialog_up.close()
			},
			//确认退出
			confirm() {
				uni.$http.get('/user/logout').then(res => {
					//console.log(res)
					if (res.data.code === 200) {
						uni.clearStorage(); //清理本地缓存
						uni.redirectTo({
							url: '/pages/index/index'
						})
					}
					this.$refs.dialog_up.close();
				}).catch(err => {
					console.log(err)
				})
			},
			//请假申请
			applyLeave() {
				uni.navigateTo({
					url: '/pages/applyLeave/applyLeave'
				})
			},
			//申请记录
			allLeaves() {
				uni.navigateTo({
					url: '/pages/allLeaves/allLeaves'
				})
			},
			//销假路上
			goTerminate() {
				uni.showToast({
					title: '请在 申请记录->等待销假->我要销假 页面进行销假操作',
					icon: 'none',
				});
				setTimeout(() => {
					uni.navigateTo({
						url: '/pages/allLeaves/allLeaves?is_WAIT_REPORT=true'
					})
				}, 1000)
			},
			//看到这一坨就想吐
			onClick1() {
				this.$refs.updateOne.open()
			},
			//yue
			onClick2() {
				this.$refs.updateTwo.open()
			},
			onClick3() {
				this.$refs.updateThree.open()
			},
			onClick4() {
				this.$refs.updateFour.open()
			},

			//弹窗关闭
			closeOne() {
				this.$refs.updateOne.close();
			},
			closeTwo() {
				this.$refs.updateTwo.close();
			},
			closeThree() {
				this.$refs.updateThree.close();
			},
			closeFour() {
				this.$refs.updateFour.close();
			},
			confirmOne(e) {
				if(e == ""){
					uni.showToast({
						icon:"error",
						title: "修改值不能为空",
					})
				}else{
					this.studentMessage.phoneNumber = e;
					//提交修改
					this.submitUpdate("one");
				}
			},
			confirmTwo(e) {
				if(e == ""){
					uni.showToast({
						icon:"error",
						title: "修改值不能为空",
					})
				}else{
					this.studentMessage.parentNumber = e;
					//提交修改
					this.submitUpdate("two");
				}
			},
			confirmThree(e) {
				if(e == ""){
					uni.showToast({
						icon:"error",
						title: "修改值不能为空",
					})
				}else{
					this.studentMessage.buildingNumber = e;
					//提交修改
					this.submitUpdate("three");
				}
			},
			confirmFour(e) {
				if(e == ""){
					uni.showToast({
						icon:"error",
						title: "修改值不能为空",
					})
				}else{
					this.studentMessage.dormitoryNumber = e;
					//提交修改
					this.submitUpdate("four");
				}
			},

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
				height: 40px;
				line-height: 40px;
				font-size: $jxnu-font-14;
			}

			.revise-button {
				color: #fff;
				background-color: $jxnu-bg-color;
			}
		}
	}

	.teacher-tools {
		.tools-box {
			display: flex;
			flex-direction: row;
			justify-content: flex-start;
			text-align: center;

			.tools-btn {
				display: block;
				height: 35px;
				width: 35px;
				padding: 20rpx;
				margin: 20rpx;
				background-color: $jxnu-bg-color;
				color: $uni-text-color;
				border-radius: 30rpx;
			}

			.tools-img {
				height: 35px;
				width: 35px;
			}
		}
	}

	.list-title {
		padding: 10px;
		text-align: center;
		color: $uni-text-color-grey;
		font-size: $jxnu-font-14;
	}
</style>
