<template>
	<view class="import-list">
		<uni-segmented-control :values="current" :current="currentIndex"  @clickItem="onClickItem" active-color="#1b478e" style-type="text"></uni-segmented-control>
		<view v-show ="currentIndex === 0" class="choose-file">
			<uni-group>
				<uni-forms ref="form" :modelValue="formData" :rules="formDatarules" validateTrigger="submit">
						<uni-forms-item required label="选择年级" name="grade">
							<uni-easyinput  type="text" v-model="formData.grade" placeholder="请输入导入年级数字(如:2018)" />
						</uni-forms-item>
						<uni-forms-item required name="file" label="选择文件" >
							<uni-file-picker 
							limit="1" 
							file-mediatype="all"
							file-extname = "xlsx"
							:auto-upload="false"
							@select="selectS" 
							 >
							 </uni-file-picker>
						</uni-forms-item>
						<button @click="submitFormS">提交</button>
				</uni-forms>
			</uni-group>
		</view>
		<view v-show ="currentIndex === 1" class="choose-file">
			<uni-group>
				<uni-forms :modelValue="teacherFile">
						<uni-forms-item required name="file" label="选择文件" >
							<uni-file-picker 
							limit="1" 
							file-mediatype="all"
							file-extname = "xlsx"
							:auto-upload="false"
							@select="selectT" 
							 >
							 </uni-file-picker>
						</uni-forms-item>
						<button @click="submitFormT">提交</button>
				</uni-forms>
			</uni-group>
		</view>
		 
	</view>
</template>

<script>
	export default {
		data() {
			return {
				filename:"",
				path:"",
				studentFile:{
					filename:"",
					path:"",
				},
				teacherFile:{
					filename:"",
					path:"",
				},
				formData:{
					grade:'',
				},
				formDatarules:{
					"grade":{
						rules: [{
							required: true,
							errorMessage: '请输入导入的年级',
						},{
							format:'number',
							errorMessage: '请输入正确的年级',
						}
					]
					}
				},
				currentIndex: 0,
				current:['导入学生名单','导入教师名单'],
			}
		},
		methods: {
			onClickItem(e){
				this.currentIndex = e.currentIndex
			},
			selectS(res){
				//console.log(res.tempFilePaths[0]);
				this.studentFile.filename = res.tempFiles[0].name;
				this.studentFile.path = res.tempFilePaths[0];
			},
			selectT(res){
				console.log(res.tempFilePaths[0]);
				this.teacherFile.filename = res.tempFiles[0].name;
				this.teacherFile.path = res.tempFilePaths[0];
			},
			submitFormS(){
			this.$refs.form.validate().then(res=>{
				if(this.studentFile.path){
					//直接使用blob路径上传文件
						uni.uploadFile({
							url: "https://www.lovehot.club/api/excel/upload?grade=" + this.formData.grade,
							filePath: this.studentFile.path,
							header: {
								"token" : uni.getStorageSync('token'),
								//"Content-Type": "multipart/form-data",
							},
							success: (res) => {
								let obj = JSON.parse(res.data);
								console.log(obj)
								if(obj.code === 200){
									uni.showToast({
										icon:'success',
										title: '上传成功'
									});
									setTimeout(()=>{
										uni.redirectTo({
											url: '/pages/importList/importList'
										});
									},1500)
								}else{
									uni.showToast({
										icon:'error',
										title: '文件提交失败'
									});
									
								}
							},
							fail: (err) => {
								uni.showToast({
									icon:'error',
									title: '请求失败'
								});
							},
						})
					
				}else{
					uni.showToast({
						title:'请选择上传文件',
						icon:'none',
					})
				}
				})
			},
			submitFormT(){
				if(this.teacherFile.path){
					console.log(uni.getStorageSync('token'))
					uni.uploadFile({
						url: "https://www.lovehot.club/api/excel/uploadTeacher",
						filePath: this.teacherFile.path,
						header: {
							"token" : uni.getStorageSync('token'),
							//"Content-Type": "multipart/form-data",
						},
						success: (res) => {
							let obj = JSON.parse(res.data);
							console.log(obj)
							if(obj.code === 200){
								uni.showToast({
									icon:'success',
									title: '上传成功'
								});
							}else{
								uni.showToast({
									icon:'error',
									title: '文件上传失败'
								});
							}
						},
						fail: (err) => {
							uni.showToast({
								icon:'error',
								title: '请求失败'
							});
						},
					})
				}else{
					uni.showToast({
						title:'请选择上传文件',
						icon:'none',
					})
				}	
			},
			//BlobUrl转blob数据
			// objectURLToBlob(blodurl) {
			// 	uni.showLoading({
			// 		title: '压缩中...'
			// 	});
			// 	return new Promise((resolve, reject) => {
			// 		var http = new XMLHttpRequest();
			// 		http.open('GET', blodurl, true);
			// 		http.responseType = 'blob';
			// 		http.onload = function(e) {
			// 		    if (this.status == 200 || this.status === 0) {
			// 			// console.log('blod数据',this.response);
			// 			// 在将blod数据转为file
			// 			let files = new window.File([this.response], 'file', { type: 'xlsx' });
			// 			// console.log('blod数据转换file',files);
			// 			resolve(files);
			// 			uni.hideLoading();
			// 	        }
			// 		};
			// 		http.send();
			// 	});
			// },
		}
		}
</script>

<style lang="scss">
	.import-list{
		width: 95vw;
		margin:0 auto;
		button{
			background-color: $jxnu-bg-color;
			color: aliceblue;		
		}
	}
	
</style>
