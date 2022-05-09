<template>
	<view>
		<view class="apply-leave">
			<uni-forms ref="leave_from" :modelValue="leaveData" :rules="leaveDataRules" class="form-style" :border="true" validateTrigger="bind" err-show-type="toast">
				<view class="warning-tips">
					<text>注意：以下内容请如实填写，内容真实，如有隐瞒，需承担相应责任！</text>
				</view>
				<!-- 离校确认 -->
				<view class="leave-from">
					<uni-forms-item required name="left" label="是否离校" >
						<view class="switch">
							<switch @change="switchChange1" checked color="#1b478e"/>
						</view>
					</uni-forms-item>
					<view v-if="leaveData.depart">
					<uni-forms-item required name="leaveTime" label="实际离校时间" >
						<uni-datetime-picker type="datetime" v-model="leaveData.leaveTime" :border="false" :clear-icon="false"  placeholder="选择实际返校时间" />
					</uni-forms-item>
					<uni-forms-item required name="departWay" label="交通情况说明" >
						<view class="switch">
							<uni-easyinput type="textarea" autoHeight v-model="leaveData.departWay" placeholder="请输入交通情况说明,如乘坐车辆的车牌号、司机姓名等" :inputBorder="false" class="input-textarea"></uni-easyinput>
						</view>
					</uni-forms-item>
				</view>
				</view>
			</uni-forms>
			<!--返校确认 -->
			<uni-forms ref="back_from" :modelValue="backData" :rules="backDataRules" class="form-style" :border="true" validateTrigger="bind" err-show-type="toast">
				<view class="leave-from">
					<uni-forms-item required name="left" label="是否返校" >
						<view class="switch">
							<switch @change="switchChange2" checked color="#1b478e"/>
						</view>
					</uni-forms-item>
					<view v-if="backData.back">
					<uni-forms-item required name="backTime" label="实际返校时间" >
						<uni-datetime-picker type="datetime" v-model="backData.backTime" :border="false" :clear-icon="false" placeholder="选择实际返校时间" />
					</uni-forms-item>
					<uni-forms-item required name="backWay" label="交通情况说明" >
						<view class="switch">
							<uni-easyinput type="textarea" autoHeight v-model="backData.backWay" placeholder="请输入交通情况说明,如乘坐车辆的车牌号、司机姓名等" :inputBorder="false" class="input-textarea"></uni-easyinput>
						</view>
					</uni-forms-item>
				</view>
				</view>
				<button @click="submitForm">提交</button>
			</uni-forms>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				id:'',
				leaveData:{
					"depart": "true",
					"leaveTime": "",
					"departWay": ""
				},
				backData:{
					"back" : "true",
					"backTime": "",
					"backWay": "",
				},
				leaveDataRules:{
				},
				backDataRules:{
				}
			}
		},
		onLoad(item){
			this.id = item.id;
			console.log(this.id)
		},
		methods: {
			switchChange1(e){
				console.log(e.detail.value)
				this.leaveData.depart = e.detail.value;
				if(this.leaveData.depart === true){
					this.leaveDataRules = {
							"leaveTime":{
								rules:[
									{
										required: true,
										errorMessage: "请输入实际离校时间"
									}]
							},
							"departWay":{
								rules:[
									{
										required: true,
										errorMessage: "请输入交通情况说明"
									}]
							},
						}
				}else{
					this.leaveDataRules = {}
				}
				
			},
			switchChange2(e){
					console.log(e.detail.value)
					this.backData.back = e.detail.value;
					if(this.backData.back === true){
						this.backDataRules = {
								"backTime":{
									rules:[
										{
											required: true,
											errorMessage: "请输入实际返校时间"
										}]
								},
								"backWay":{
									rules:[
										{
											required: true,
											errorMessage: "请输入交通情况说明"
										}]
								},
							}
					}else{
						this.backDataRules = {}
					}
					
			},
			submitForm(){
					this.$refs.form.validate().then(res => {
						uni.showToast({
							title: "提交成功"
						})
						setTimeout(() => {
							uni.navigateTo({
								url: '/pages/studentHome/studentHome'
							})
						}, 1000);
					}).catch(err => {
						console.log('err' + err);
					})
				}
			}
		}
</script>

<style lang="scss">
$w:90vw;
	$h:35vh;
	.apply-leave{
		width: $w;
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		margin: 50rpx auto;
		.warning-tips{
			margin: 50rpx auto;
			font-size: $jxnu-font-14;
			color: $uni-color-error;
		}
		.leave-from{
			width: $w;
			height: $h;
		}
		.switch{
			margin-left: 20rpx;
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
