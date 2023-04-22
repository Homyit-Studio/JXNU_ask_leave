<template>
	<view class="file-picker-box">
		<uni-group>
			<text>请假凭证(非必填，上传图片数量无限制)</text>
				<uni-file-picker
					:value="imageValue" 
					return-type="array"
					file-mediatype="image"
					mode="grid" 
					file-extname="png,jpg,jpeg"
					:auto-upload="false"
					@select="select" 
					@delete="handleDelete" 
					class="file-picker"
				/>
		</uni-group>
		<button @click="submitForm">提交</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				imageValue:[],
				id:'',
			    type:''
			}
		},
		onLoad(item){
			this.id = item.id;
			this.type = item.type

		},
		methods: {
			// 获取上传状态
			select(res){
				//console.log('选择文件：',e)
				//this.studentFile.filename = res.tempFiles[0].name;
				//console.log(res.tempFilePaths)
				for(let index in res.tempFilePaths){
					this.imageValue.push(res.tempFilePaths[index])

				}
				//console.log(res.tempFilePaths[0])
				//console.log(this.imageValue)
			},
			//图片删除
			handleDelete(e){
				const num = this.imageValue.findIndex(v => v.url === e.tempFilePath);
				this.imageValue.splice(num, 1);
			},
			submitForm(){
				if(this.imageValue.length != 0){
					uni.$http.get('/image/delete/' + this.id).then(res =>{
						//console.log(res)
						if(res.data.code === 200){
							this.postImg(uni.getStorageSync('token'))
						}else{
							uni.showToast({
								icon:'none',
								title:"网络似乎出现了一些问题"
							})
						}
					}).catch(err =>{
						uni.showToast({
							icon:'none',
							title:"网络似乎出现了一些问题"
						})
					})
					
				}else{
					uni.showToast({
						icon:'none',
						title:"请选择要上传的图片"
					})
				}
			},
			async postImg(token) {
			    if (!this.imageValue.length){
					uni.showToast({
						icon:'success',
						title: '附件添加成功'
					});	
					return;
				} 
			    const path = this.imageValue.pop();
				console.log(path)
				//console.log(this.imageValue[0])
				await uni.uploadFile({
						url: "https://blog.aday.top/api/image/uploadFiles?id=" + this.id,
						filePath: path,
						name: 'files',//后端接收字段名
						header: {
							"token" : token,
							//"Content-Type": "multipart/form-data",
						},
						success: (res) => {
							let obj = JSON.parse(res.data);
							// console.log(res)
							if(obj.code === 200){
								uni.redirectTo({
									url: `/pages/allLeaveDetails/allLeaveDetails?id=` + this.id + '&type=' + this.type ,
									animationType: 'pop-in',
									animationDuration: 200
								})
							}else{
								uni.showToast({
									icon:'error',
									title: '提交失败'
								});									
							}
						},
						fail: (err) => {
							uni.showToast({
								icon:'error',
								title: '提交失败'
							});	
						},
					})
			    this.postImg(token);
			},
		}
	}
</script>

<style lang="scss">
	.file-picker-box{
		width: 90vw;
		height: 45vh;
		margin: 30rpx auto;
		// margin-top: 5vh;
		font-size: $jxnu-font-14;
		color: $uni-text-color;
		.file-picker{
			padding-top: 30rpx;
		}
		button{
			background-color: $jxnu-bg-color;
			color: aliceblue;
			margin-top: 5vh;				
		}
	}
</style>
