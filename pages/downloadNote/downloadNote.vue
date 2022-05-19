<template>
	<view class="download-note">
		<uni-group>
			<uni-forms ref="form" :modelValue="formData" :rules="formDatarules" validateTrigger="submit">
					<uni-forms-item required label="选择年级" name="gradeId">
						<uni-easyinput  type="text" v-model="formData.gradeId" placeholder="请输入年级数字(如:2018)" />
					</uni-forms-item>
					<uni-forms-item required name="startTime" label="选择起始时间" >
						<uni-datetime-picker type="datetime" v-model="formData.startTime" :border="false"  :clear-icon="false"  placeholder="选择起始日期和时间"/>		
					</uni-forms-item>
					<uni-forms-item required name="endTime" label="选择结束时间" >
						<uni-datetime-picker type="datetime" v-model="formData.endTime" :border="false"  :clear-icon="false"  placeholder="选择结束日期和时间"/>
					</uni-forms-item>
					<button @click="submitForm()">获取假条</button>
			</uni-forms>
		</uni-group>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				json_array:"",
				formData:{
					"startTime": "",
					"endTime": "",
					"gradeId": "",	
				},
				formDatarules:{
					"gradeId":{
						rules: [{
							format:'number',
							errorMessage: '请输入正确的年级',
						}
					]
					},
					"startTime":{
						rules: [{
						}
					]
					},
					"endTime":{
						rules: [{
						}
					]
					},
				},
			}
		},
		onLoad(){
			//this.formData.startTime = this.formData.endTime = this.getFormatDate();
		},
		methods: {
			submitForm(){
				// this.formData.startTime = this.dateAdd(this.formData.startTime);
				// this.formData.endTime = this.dateAdd(this.formData.endTime);
				this.$refs.form.validate().then(res=>{
					if(uni.getSystemInfoSync({}).platform.match(/(ios|android|mac)/i)){
						this.platformnotH5()//移动端
					}else{
						this.platformH5()//h5
					}
				}).catch(err=>{
					
				})
			},
			//H5环境文件下载
			platformH5(){
				uni.request({
					url: 'http://101.43.85.67:8081/excel/downloadNote1?gradeId='+this.formData.gradeId+'&startTime='+this.formData.startTime+'&endTime='+this.formData.endTime,
					responseType: 'ArrayBuffer',
					header: {
						"token" : uni.getStorageSync('token'),
					},
					success: (res) => {
						console.log(res)
						if(res.statusCode === 200){	
							this.getFile(res.data)
						}		
					}
				});
			},
			//非H5环境下文件下载
			platformnotH5(){
				uni.downloadFile({
					url: 'http://101.43.85.67:8081/excel/downloadNote1?gradeId='+this.formData.gradeId+'&startTime='+this.formData.startTime+'&endTime='+this.formData.endTime, 
					header: {
						"token" : uni.getStorageSync('token'),
						//"Content-Type": "multipart/form-data",
					},
					success: (res) => {
						console.log(res)
						if (res.statusCode === 200) {
							uni.saveFile({
								empFilePath:res.tempFilePath,//下载成功之后返回的临时路径
								success:(e)=>{
								//保存成功之后 打开文件
								uni.openDocument({
								  filePath: e.savedFilePath,
								  fail:(e)=>{
									  console.log(e)
									uni.showToast({
									  title: `打开失败`
									})
								  }
								})
							  },
							  fail:(e)=>{
								  console.log(e)
								uni.showToast({
								  title: `保存失败`
								})
							  }
							})
						}
					}
				});
				
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
				getFile(data){
				let blob = new Blob([data], {type: `application/xlsx;charset=utf-8`});
					// 获取heads中的filename文件名
					let downloadElement = document.createElement('a');
					// 创建下载的链接
					let href = window.URL.createObjectURL(blob);
					downloadElement.href = href;
					// 下载后文件名
					downloadElement.download = "假条.xlsx";
					document.body.appendChild(downloadElement);
					// 点击下载
					downloadElement.click();
					// 下载完成移除元素
					document.body.removeChild(downloadElement);
					// 释放掉blob对象
					window.URL.revokeObjectURL(href);
			
		}}
		
	}
</script>

<style lang="scss">
	.download-note{
		width: 95vw;
		margin:0 auto;
		button{
			background-color: $jxnu-bg-color;
			color: aliceblue;		
		}
	}
	@media screen and (min-width:950px){
		.download-note{
			width: 900px;
		}
	}
</style>
