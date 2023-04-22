<template>
	<view class="download-note">
		<uni-group>
			<uni-forms ref="form" :modelValue="formData" :rules="formDatarules" validateTrigger="submit" class="download-form">
					<uni-forms-item required label="范围选择" name="gradeId">
						<picker @change="bindPickerChange" :value="index" :range="array">
							<view class="uni-input">
								<uni-tag :text="array[index]" type="primary" />	
							</view>
						</picker>	
					</uni-forms-item>
					<uni-forms-item required label="选择年级" name="gradeId" v-if="index == 0">
						<uni-easyinput  type="text" v-model="formData.gradeId" placeholder="请输入年级数字(如:2018)" />
					</uni-forms-item>
					<uni-forms-item required label="选择班级" name="classId" v-else-if="index == 1">
						<uni-easyinput  type="text" v-model="formData.classId" placeholder="请输入班级id" />
					</uni-forms-item>
					<uni-forms-item required label="选择个人" name="studentNumber" v-else-if="index == 2">
						<uni-easyinput  type="text" v-model="formData.studentNumber" placeholder="请输入学生学号" />
					</uni-forms-item>
					<uni-forms-item required name="startTime" label="选择起始时间" >
						<uni-datetime-picker type="datetime" v-model="formData.startTime" :border="false"  :clear-icon="false"  placeholder="选择起始日期和时间"/>		
					</uni-forms-item>
					<uni-forms-item required name="endTime" label="选择结束时间" >
						<uni-datetime-picker type="datetime" v-model="formData.endTime" :border="false"  :clear-icon="false"  placeholder="选择结束日期和时间"/>
					</uni-forms-item>
					<button @click="submitForm()" class="submit-button">获取假条</button>
			</uni-forms>
		</uni-group>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				dataPath:"",
				index: 0,
				array:["年级","班级","个人"],
				formData:{
					"startTime": "",
					"endTime": "",
					"gradeId": "",
					"classId":"",
					"studentNumber":""
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
			bindPickerChange(e){
				//console.log(e)
				//清空数据
				this.formData.classId = this.formData.gradeId = this.formData.studentNumber;
				this.index = e.detail.value;
			},
			submitForm(){
				// this.formData.startTime = this.dateAdd(this.formData.startTime);
				// this.formData.endTime = this.dateAdd(this.formData.endTime);
				this.$refs.form.validate().then(res=>{
					this.platformnotH5()//移动端
					// if(uni.getSystemInfoSync({}).platform.match(/(ios|android|mac)/i)){
					// 	this.platformnotH5()//移动端
					// }else{
					// 	this.platformH5()//h5
					// }
				}).catch(err=>{
				})
			},
			//H5环境文件下载
			platformH5(){
				uni.request({
					url: 'https://leave.jxnu.edu.cn/api/excel/downloadNote1?gradeId='+this.formData.gradeId+'&startTime='+this.formData.startTime+'&classId='+this.formData.classId+'&studentNumber='+this.formData.studentNumber,
					responseType: 'ArrayBuffer',
					header: {
						"token" : uni.getStorageSync('token'),
					},
					success: (res) => {
						//console.log(res)
						if(res.statusCode === 200){	
							this.getFile(res.data)
						}		
					}
				});
			},
			//非H5环境下文件下载
			platformnotH5(){
				//console.log(123123)
				//const that = this;
				wx.request({
					url: 'https://blog.aday.top/api/excel/downloadNote1?gradeId='+this.formData.gradeId+'&startTime='+this.formData.startTime+'&classId='+this.formData.classId+'&studentNumber='+this.formData.studentNumber,
					header: {
						"token" : uni.getStorageSync('token'),
						//"Content-Type": "multipart/form-data",
					},
					responseType: "arraybuffer",
					success: (res) => {
								//console.log("下载成功！", res);
						        var fileManager = wx.getFileSystemManager();
						        var FilePath = wx.env.USER_DATA_PATH + "/" + "假条.xlsx";
						        fileManager.writeFile({
						          data: res.data,
						          filePath: FilePath,
						          encoding: "binary", //编码方式 
						          success: res => {
									//console.log('编码格式');
									wx.openDocument({ //我这里成功之后直接打开
									  filePath: FilePath, 
									  showMenu:true,
									  fileType: "xlsx",
									  success: res => {
										//console.log("打开文档成功");
										uni.showToast({
											icon:"success",
											title:"文档打开成功"
										})
									  },
									  fail: err => {
										//console.log("打开文档失败", err);
										uni.showToast({
											icon:"error",
											title:"文档打开失败"
										})
									  }
						    });
						}
					})
					},
					fail:(err)=>{
						uni.showToast({
							icon:"error",
							title:"文件下载失败，请检查时间格式是否正确"
						})
					}
				})	
			},
			//补充时间
			dateAdd(dateStr){
				if(dateStr.length <= 10){
					//console.log(dateStr + '00:00:00')
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
					downloadElement.download = "下载假条.xlsx";
					document.body.appendChild(downloadElement);
					// 点击下载
					downloadElement.click();
					// 下载完成移除元素
					document.body.removeChild(downloadElement);
					// 释放掉blob对象
					window.URL.revokeObjectURL(href);
			
			}
			},
		}
</script>

<style lang="scss">
	.download-note{
		width: 95vw;
		margin:0 auto;
		text-align: center;
	}
	.uni-input{
		margin-top: 8px;
	}
	.submit-button{
		background-color: $jxnu-bg-color;
		color: aliceblue;		
	}
	@media screen and (min-width:950px){
		.download-note{
			width: 900px;
		}
	}
</style>