<template>
	<view>
		<uni-notice-bar scrollable="true" single="true" text="为落实落细防疫工作,请各位同学在离校和返校后进行假条销假。如未出行，也请在假条销假界面中填写返校内容。" showIcon></uni-notice-bar>
	<view class="apply-leave">
		<view>
			<uni-forms ref="form" :modelValue="formData" :rules="dataRules" class="form-style" :border="true" validateTrigger="bind" err-show-type="toast">
				<uni-group>
					<uni-forms-item required name="leave" label="是否离校" >
						<view class="switch">
							<switch @change="switchChange" color="#1b478e"/>
						</view>
					</uni-forms-item>
				</uni-group>
				<uni-group>
					<uni-forms-item required name="destination" label="目的地">
						<uni-easyinput multiple v-model="formData.destination"  placeholder="请输入目的地" :inputBorder="false"/>
					</uni-forms-item>
				</uni-group>
				<uni-group>
					<uni-forms-item required name="startTime" label="开始时间" >
						<uni-datetime-picker type="datetime" v-model="formData.startTime" :border="false" :clear-icon="false"  placeholder="选择离校日期和时间" />
					</uni-forms-item>
				</uni-group>
				<uni-group>
					<uni-forms-item required name="endTime" label="结束时间" >
						<uni-datetime-picker type="datetime" v-model="formData.endTime" :border="false"  :clear-icon="false"  placeholder="选择返校日期和时间"/>
					</uni-forms-item>
				</uni-group>
				<uni-group>
					<uni-forms-item required name="way" label="交通方式" >
						<uni-easyinput multiple v-model="formData.way"  placeholder="请输入交通方式" :inputBorder="false"/>
					</uni-forms-item>
				</uni-group>
				<uni-group>	
					<uni-forms-item required name="reason" label="申请事由" >
						<view class="switch">
							<uni-easyinput type="textarea" autoHeight v-model="formData.reason" placeholder="请输入内容" :inputBorder="false" class="input-textarea"></uni-easyinput>
						</view>
					</uni-forms-item>
				</uni-group>
			</uni-forms>
			<button @click="submitForm">提交</button>
		</view>
	<!-- 	<view v-show="current === 1" class="file-picker-box">
			<text>请假凭证(非必填，最多上传3张图片)</text>
			<uni-file-picker
				v-model="imageValue" 
				return-type="array"
				file-mediatype="image"
				mode="grid" 
				file-extname="png,jpg"
				:limit="3"
				:auto-upload="false"
				@select="select" 
				 @delete="handleDelete" 
				class="file-picker"
			/>
		</view> -->
		<view>
			
		</view>
	</view>
	</view>
</template>

<script>
	//import errShow from'./utils/errShowToast.js'
	export default {
		data() {
			return {
					current:0,
					value:['上一页','下一页'],
					imageValue:[
					],
					studentMsg:{
						//学生信息
					},
					formData:{
						applicant:"",//姓名
						majorAndClass:'',//班级
						leave:false,//是否离开学校
						destination:'',//目的地
						way:'',//交通方式
						startTime:'',//起始时间
						endTime:'',//结束时间
						reason:'',//请假事由,
						dormitoryNumber:'',
						phoneNumber:'',
						buildingNumber:'',//宿舍楼栋
					},
					dataRules:{
						// "dormitoryNumber":{
						// 	rules:[
						// 		{
						// 			required: true,
						// 			errorMessage: "请输入宿舍号"
						// 		}]
						// },
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
						// "phoneNumber":{
						// 	rules:[
						// 		{
						// 			required: true,
						// 			errorMessage: "请输入手机号"
						// 		}]
						// },
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
			// 获取上传状态
			select(res){
				//console.log('选择文件：',e)
				//this.studentFile.filename = res.tempFiles[0].name;
				this.imageValue.push(res.tempFilePaths[0])
				// console.log(res.tempFilePaths[0])
				//console.log(this.imageValue)
			},
			//图片删除
			handleDelete(e){
				const num = this.imageValue.findIndex(v => v.url === e.tempFilePath);
				this.imageValue.splice(num, 1);
				//console.log(this.imageValue)
			},
			onClickItem(e){
				this.current = e.currentIndex;
			},
			submitForm(){
				//console.log(this.formData.leave === true ? "YES" : "NO")
				//console.log(this.formData.startTime)
				this.formData.startTime = this.dateAdd(this.formData.startTime);
				this.formData.endTime = this.dateAdd(this.formData.endTime);
				//console.log(this.formData.startTime)
				this.$refs.form.validate().then(res=>{	
					uni.$http.post('/leave/ask', {
							"startTime":this.formData.startTime,
							"endTime": this.formData.endTime,
							"depart": this.formData.leave === true ? "YES" : "NO",
							"destination": this.formData.destination,
							"way": this.formData.way,
							"reason": this.formData.reason
					}).then((res)=>{
						console.log(res)
						if(res.data.code === 200){
							this.formData.studentNumber = this.studentMsg.studentNumber;
							this.formData.majorAndClass = this.studentMsg.majorAndClass;
							this.formData.dormitoryNumber = this.studentMsg.dormitoryNumber;
							this.formData.buildingNumber = this.studentMsg.buildingNumber;
							this.formData.phoneNumber = this.studentMsg.phoneNumber;
							console.log(this.studentMsg)
							this.formData.leave = this.formData.leave === true ? '是' : '否';
							//this.uploadImg()
							uni.redirectTo({
								url:'../finishLeave/finishLeave?formData=' + encodeURIComponent(JSON.stringify(this.formData))
							})
						}else{
							
						}
					}).catch((err)=>{
						
					})			
				}).catch(err =>{
					this.current = 0;
				})
				
			},
			// async uploadImg(tempFilePaths, token) {
			//     console.log(token)
			//     if (!tempFilePaths.length) return;
			//     const path = tempFilePaths.pop();
			//     this.filePathsList.push({url:path,name:""})
			//     const [err, {data}] = await uni.uploadFile({
			//         url: 'https://localhost/file/api/uploadtemp',
			//         filePath: path,
			//         name: 'file',
			//         header: {
			//             Authorization: token,
			//             "Content-Type": "multipart/form-data",
			//         }
			//     });
			//     console.log("err", err)
			//     console.log("data", data)
			//     if (!this.isGuid(data)) {
			//         // upload fail
			//         this.filePathsList.pop()
			//         uni.showToast({
			//             title: "上传失败",
			//             icon: "none"
			//         })
			//     }else{
			//         // upload success
			//         this.filePathsList[this.filePathsList.length - 1].name = data
			//     }
			//     this.uploadImg(tempFilePaths,token);
			// },
			switchChange(e){
				//console.log(e.detail.value);
				this.formData.leave = e.detail.value;
			},
			//补充时间
			dateAdd(dateStr){
				if(dateStr.length <= 11){
					console.log(dateStr + '00:00:00')
					return dateStr + '00:00:00'
				}
				else{
					return dateStr
				}
			},
			//获取当前格式化时间
			getFormatDate() {
			     var date = new Date();
			     var sign1 = "-";
			     var sign2 = ":";
			     var year = date.getFullYear() // 年
			     var month = date.getMonth() + 1; // 月
			     var day  = date.getDate(); // 日
			     var hour = date.getHours(); // 时
			     var minutes = date.getMinutes(); // 分
			     var seconds = date.getSeconds() //秒
			     // 给一位数数据前面加 “0”
			     if (month >= 1 && month <= 9) {
			      month = "0" + month;
			     }
			     if (day >= 0 && day <= 9) {
			      day = "0" + day;
			     }
			     if (hour >= 0 && hour <= 9) {
			      hour = "0" + hour;
			     }
			     if (minutes >= 0 && minutes <= 9) {
			      minutes = "0" + minutes;
			     }
			     if (seconds >= 0 && seconds <= 9) {
			      seconds = "0" + seconds;
			     }
			     var currentdate = year + sign1 + month + sign1 + day + " " + hour + sign2 + minutes + sign2 + seconds;
			     return currentdate;
		},
		},
		onLoad:function(options) {
			let userData = JSON.parse(uni.getStorageSync('userStr'));
			this.studentMsg = userData;
			console.log(this.getFormatDate())
			this.formData.startTime = this.formData.endTime = this.getFormatDate();
		},
	}
</script>

<style lang="scss" scoped>
	$w:90vw;
	.apply-leave{
		width: $w;
		display: flex;
		position: relative;
		justify-content: center;
		flex-wrap: wrap;
		margin: 0 auto;
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
			width: 350rpx;
			background-color: $jxnu-bg-color;
			color: aliceblue;
			margin-top: 5vh;				
		}
	}
	.segmented-control{
		height: 25px;
		width: 200rpx;
		margin: 20px auto;
	}
	.file-picker-box{
		width: $w;
		height: 45vh;
		margin: 30rpx auto;
		// margin-top: 5vh;
		font-size: $jxnu-font-14;
		color: $uni-text-color;
		.file-picker{
			padding-top: 30rpx;
		}
	}
	@media screen and (min-width:950px){
		.apply-leave{
			width: 900px;
			.form-style{
				width: 900px;
			}
			// button{
			// 	width: 900px
			// }
		}
	}
</style>
