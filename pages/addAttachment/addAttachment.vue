<template>
	<view class="file-picker-box">
		<uni-group>
			<text>请假凭证(非必填，最多上传3张图片)</text>
				<uni-file-picker
					:value="imageValue" 
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
		</uni-group>
		<button @click="submitForm">提交</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				imageValue:[],
				id:''
			}
		},
		onLoad(item){
			this.id = item.id;	
		},
		methods: {
			// 获取上传状态
			select(res){
				//console.log('选择文件：',e)
				//this.studentFile.filename = res.tempFiles[0].name;
				this.imageValue.push(res.tempFilePaths[0])
				// console.log(res.tempFilePaths[0])
				console.log(this.imageValue)
			},
			//图片删除
			handleDelete(e){
				const num = this.imageValue.findIndex(v => v.url === e.tempFilePath);
				this.imageValue.splice(num, 1);
				console.log(this.imageValue)
			},
			submitForm(){
				if(this.imageValue){
					this.postImg(uni.getStorageSync('token'))
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
				//console.log(path)
				//console.log(this.imageValue[0])
			    await uni.uploadFile({
						url: "http://101.43.85.67:8081/image/uploadFiles?id=" + this.id,
						filePath: path,
						name: 'files',//后端接收字段名
						header: {
							"token" : token,
							//"Content-Type": "multipart/form-data",
						},
						success: (res) => {
							let obj = JSON.parse(res.data);
							console.log(obj)
							if(obj.code === 200){
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
