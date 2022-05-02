<template>
	<view>
		<uni-forms :modelValue="formData" :rules="dataRules" >
			<uni-forms-item required label="姓名" name="applicant">
				<uni-easyinput type="text" v-model="formData.applicant" placeholder="请输入姓名" />
			</uni-forms-item>
			<uni-forms-item required name="majorAndClass" label="班级" >
				<uni-easyinput multiple v-model="formData.majorAndClass"  placeholder="请输入班级"/>
			</uni-forms-item>
		</uni-forms>
		<button @click="submitForm">Submit</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				formData:{
					applicant:"",//姓名
					majorAndClass:'',//班级
					leave:'',//是否离开学校
					destination:'',//目的地
					dormitoryNumber:'',//宿舍号
					way:'',//交通方式
					phoneNumber:'',//手机号
				},
				dataRules:{
					applicant:{
						rules:[
							// 校验 name 不能为空
							{
								required: true,
								message: '请填写姓名',
							}]
						}
					}
				}
		},
		methods:{
			submitForm(){}
		}
	}
</script>

<style>
</style>
