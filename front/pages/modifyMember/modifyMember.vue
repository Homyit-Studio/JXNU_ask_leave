<template>
	<view class="add-member">
		<uni-forms ref="form" :modelValue="formData" class="form-style" :border="true" validateTrigger="bind" :rules="addRules"
			err-show-type="toast">
			<view>
				<uni-forms-item required label="姓名" name="username">
					<uni-easyinput multiple v-model="formData.username" placeholder="请输入成员的姓名" :inputBorder="false" />
				</uni-forms-item>
				<uni-forms-item required label="学号" name="studentnumber">
					<uni-easyinput multiple v-model="formData.studentNumber" placeholder="请输入学号或者工号" :inputBorder="false" />
				</uni-forms-item>
				<uni-forms-item label="身份" required name="roles">
					<uni-data-checkbox v-model="formData.role" :localdata="roles" />
				</uni-forms-item>
				<uni-forms-item required label="设置密码" name="password">
					<uni-easyinput multiple v-model="formData.password" placeholder="请输入成员默认密码" :inputBorder="false" />
				</uni-forms-item>
				<uni-forms-item required label="电话" name="phonenumber">
					<uni-easyinput multiple v-model="formData.phoneNumber" placeholder="请输入成员联系方式" :inputBorder="false" />
				</uni-forms-item>
				<uni-forms-item required label="性别" name="sex">
					<uni-data-checkbox v-model="formData.sex" :localdata="sexs" />
				</uni-forms-item>
			</view>
			<view v-if="peoplestatus == 'STUDENT'">
				<uni-forms-item label="民族">
					<uni-easyinput multiple v-model="formData.nation" placeholder="请输入学生民族民族" :inputBorder="false" />
				</uni-forms-item>
				<uni-forms-item label="班级">
					<uni-easyinput multiple v-model="formData.majorAndClass" placeholder="请输入学生所在班级" :inputBorder="false" />
				</uni-forms-item>
				<uni-forms-item label="父母电话">
					<uni-easyinput multiple v-model="formData.parentNumber" placeholder="请输入学生所在班级" :inputBorder="false" />
				</uni-forms-item>
				<uni-forms-item label="楼栋">
					<uni-easyinput multiple v-model="formData.buildingNumber" placeholder="请输入学生宿舍如3栋S142"
						:inputBorder="false" />
				</uni-forms-item>
				<uni-forms-item label="出生地">
					<uni-easyinput multiple v-model="formData.nativePlace" placeholder="请输入学生生源地" :inputBorder="false" />
				</uni-forms-item>
				<uni-forms-item label="家庭地址">
					<uni-easyinput multiple v-model="formData.homeAddress" placeholder="请输入学生家庭地址" :inputBorder="false" />
				</uni-forms-item>
			</view>
			<button @click="submitForm('form')" class="confirm-button">确认</button>
		</uni-forms>
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
				formData: {
					"classId": null,
					"gradeId": null,
					"password": null,
					"role": null,
					"username": null,
					"idNumber": null,
					"nation": null,
					"studentNumber": null,
					"sex": null,
					"nativePlace": null,
					"homeAddress": null,
					"majorAndClass": null,
					"phoneNumber": null,
					"parentNumber": null,
					"buildingNumber": null,
					"dormitoryNumber": null
				},
				roles: [{
						text: '学生',
						value: 'STUDENT'
					}, {
						text: '班主任',
						value: 'INSTRUTCTOR'
					}, {
						text: '负责人',
						value: 'SECRETART'
					},
					{
						text: '院长',
						value: 'DEAN'
					},
					{
						text: '管理人员',
						value: 'LOOK'
					}
				],
				addRules:{
					"username": {
						rules: [{
							required: true,
							errorMessage: "请输入用户名"
						}]
					},
					"roles": {
						rules: [{
							required: true,
							errorMessage: "请选择身份"
						}]
					},
					"studentnumber": {
						rules: [{
							required: true,
							errorMessage: "请输入学号或工号"
						}]
					},
					"password": {
						rules: [{
							required: true,
							errorMessage: "请输入密码"
						}]
					},
					"phonenumber": {
						rules: [{
							required: true,
							errorMessage: "请输入电话号码"
						}]
					},
					"sex": {
						rules: [{
							required: true,
							errorMessage: "请选择性别"
						}]
					}
				},
				sexs: [{
					text: '男',
					value: 'MAN'
				}, {
					text: '女',
					value: 'WOMAN'
				}],
				msg: {
					msgType: 'success',
					messageText: '这是一条成功提示',
				},
				peoplestatus:"",
				username:null
			}
		},
		onLoad(options) {
			// console.log(options.message.gradeId)
			this.peoplestatus = options.people
			this.username = options.username
		},
		methods: {
			submitForm(ref) {
				this.$refs[ref].validate().then(res => {
						// console.log(this.formData)
						uni.$http.post("/user/addUser", this.formData).then(res => {
							if (res.data.code == 200) {
								// console.log(34)
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
	.add-member {
		width: 90vw;
		margin: 0 auto;
		.confirm-button{
			width: 400rpx;
			height: 40px;
			line-height: 40px;
			margin-top: 20px;
			background-color: $jxnu-bg-color;
			color: #fff;
		}
	}
</style>
