<template>
	<view class="file-picker-box">
		<uni-group>
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
		</uni-group>
		<button @click="submitForm">提交</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				imageValue:[],
			}
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
				
			}
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
