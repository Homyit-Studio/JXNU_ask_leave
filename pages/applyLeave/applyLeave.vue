<template>
	<view class="apply-leave">
		<uni-forms  ref="form" :modelValue="formData" :rules="dataRules" class="form-style" :border="true" validateTrigger="bind" err-show-type="toast">
			<uni-forms-item required name="dormitoryNumber" label="宿舍号" >
				<uni-easyinput multiple v-model="formData.dormitoryNumber"  placeholder="请输入宿舍号,格式如7栋NF120" :inputBorder="false"/>
			</uni-forms-item>
			<uni-forms-item required name="leave" label="是否离校" >
				<view class="switch">
					<switch @change="switchChange" color="#1b478e"/>
				</view>
			</uni-forms-item>
			<uni-forms-item required name="destination" label="目的地" >
				<uni-easyinput multiple v-model="formData.destination"  placeholder="请输入目的地" :inputBorder="false"/>
			</uni-forms-item>
			<uni-forms-item required name="startTime" label="离校时间" >
				<uni-datetime-picker type="datetime" v-model="formData.startTime" :border="false" :clear-icon="false"  placeholder="选择离校时间" />
			</uni-forms-item>
			<uni-forms-item required name="endTime" label="返校时间" >
				<uni-datetime-picker type="datetime" v-model="formData.endTime" :border="false"  :clear-icon="false"  placeholder="选择返校时间"/>
			</uni-forms-item>
			<uni-forms-item required name="way" label="交通方式" >
				<uni-easyinput multiple v-model="formData.way"  placeholder="请输入交通方式" :inputBorder="false"/>
			</uni-forms-item>
			<uni-forms-item required name="phoneNumber" label="联系方式" >
				<uni-easyinput multiple v-model="formData.phoneNumber"  placeholder="请输入联系方式" :inputBorder="false"/>
			</uni-forms-item>
			<uni-forms-item required name="reason" label="申请事由" >
				<view class="switch">
					<uni-easyinput type="textarea" autoHeight v-model="formData.reason" placeholder="请输入内容" :inputBorder="false" class="input-textarea"></uni-easyinput>
				</view>
			</uni-forms-item>
			<button @click="submitForm">提交</button>
		</uni-forms>
		
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
						startTime:'',//起始时间
						endTime:'',//结束时间
						reason:''//请假事由
					},
					dataRules:{
						"dormitoryNumber":{
							rules:[
								{
									required: true,
									errorMessage: "请输入宿舍号"
								}]
						},
						"way":{
							rules:[
								{
									required: true,
									errorMessage: "请输入交通方式"
								}]
						},
						"destination":{
							rules:[
								{
									required: true,
									errorMessage: "请输入目的地"
								}]
						},
						"phoneNumber":{
							rules:[
								{
									required: true,
									errorMessage: "请输入手机号"
								}]
						},
						"startTime":{
							rules:[
								{
									required: true,
									errorMessage: "请设置起始时间"
								}]
						},
						"endTime":{
							rules:[
								{
									required: true,
									errorMessage: "请设置返校时间"
								}]
						},
						"reason":{
							rules:[
								{
									required: true,
									errorMessage: "请填写请假事由"
								}]
						},
					},
				}	
		},
		methods:{
			submitForm(){
				this.$refs.form.validate().then(res=>{
								uni.$emit('postformData', {
									formData: this.formData
								})
								uni.navigateTo({
									url:'../finishLeave/finishLeave?formData=' + encodeURIComponent(JSON.stringify(this.formData))
								})
							}).catch(err =>{
								console.log(err);
							})

			},
			switchChange(e){
				console.log(e.detail.value);
				this.formData.leave = e.detail.value;
			}
		}
	}
</script>

<style lang="scss" scoped>
	$w:90vw;
	.apply-leave{
		width: $w;
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		margin: 0 auto;
		margin-top: 5vh;
		.form-style{
			width: $w;
			.switch{
				margin-left: 20rpx;
			}
			.input-textarea{
				min-height: 10rpx;
			}
		}
		button{
			width: $w;
			background-color: $jxnu-bg-color;
			color: aliceblue;
			margin-top: 0.5vh;
		}
	}
	@media screen and (min-width:950px){
		.apply-leave{
			width: 900px;
			.form-style{
				width: 900px;
			}
			button{
				width: 900px
			}
		}
	}
</style>
