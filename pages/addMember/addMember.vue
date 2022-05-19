<template>
	<view class="add-member">
		<uni-forms ref="form" :modelValue="formData" class="form-style" :border="true" validateTrigger="bind"
			err-show-type="toast">
			<uni-forms-item required label="姓名">
				<uni-easyinput multiple v-model="formData.username" placeholder="请输入成员的姓名" :inputBorder="false" />
			</uni-forms-item>
			<uni-forms-item label="身份" required>
				<uni-data-checkbox v-model="formData.role" :localdata="roles" />
			</uni-forms-item>
			<uni-forms-item required label="学号/工号">
				<uni-easyinput multiple v-model="formData.studentNumber" placeholder="请输入学号或者工号" :inputBorder="false" />
			</uni-forms-item>
			<uni-forms-item required label="设置密码">
				<uni-easyinput multiple v-model="formData.password" placeholder="请输入成员默认密码" :inputBorder="false" />
			</uni-forms-item>
			<uni-forms-item required label="性别">
				<uni-data-checkbox v-model="formData.sex" :localdata="sexs" />
			</uni-forms-item>
			<uni-forms-item required label="民族">
				<uni-easyinput multiple v-model="formData.nation" placeholder="请输入成员民族" :inputBorder="false" />
			</uni-forms-item>
			<uni-forms-item required label="班级">
				<uni-easyinput multiple v-model="formData.majorAndClass" placeholder="请输入学生所在班级" :inputBorder="false" />
			</uni-forms-item>
			<uni-forms-item required label="电话">
				<uni-easyinput multiple v-model="formData.phoneNumber" placeholder="请输入学生所在班级" :inputBorder="false" />
			</uni-forms-item>
			<uni-forms-item required label="父母电话">
				<uni-easyinput multiple v-model="formData.parentNumber" placeholder="请输入学生所在班级" :inputBorder="false" />
			</uni-forms-item>
			<uni-forms-item required label="楼栋">
				<uni-easyinput multiple v-model="formData.buildingNumber" placeholder="请输入学生宿舍如3栋S142"
					:inputBorder="false" />
			</uni-forms-item>
			<uni-forms-item required label="出生地">
				<uni-easyinput multiple v-model="formData.nativePlace" placeholder="请输入学生生源地" :inputBorder="false" />
			</uni-forms-item>
			<uni-forms-item required label="家庭地址">
				<uni-easyinput multiple v-model="formData.homeAddress" placeholder="请输入学生家庭地址" :inputBorder="false" />
			</uni-forms-item>
			<button @click="submitForm">确认</button>
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
					}
				],
				sexs: [{
					text: '男',
					value: 'MAN'
				}, {
					text: '女',
					value: 'WOMAN'
				}],
				classId: null,
				msg: {
					msgType: 'success',
					messageText: '这是一条成功提示',
				},
			}
		},
		onLoad(options) {
			this.classId = options.id
		},
		methods: {
			submitForm() {
				this.formData.classId = this.classId
				uni.$http.post("/user/deletedUser", this.formData).then(res => {
					if (res.data.code == 200) {
						console.log(res)
						this.msg.msgType = "success"
						this.msg.messageText = res.data.message
						this.rosterRequest.pageNo = 1
						this.$refs.message.open()
						this.$refs.studentPopup.close()
						this.requestClassRoster(this.classId)
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
						this.$refs.studentPopup.close()
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	.add-member {
		width: 90vw;
		margin: 0 auto;
	}
</style>
