<template>
	<view class="class-list-page">
		<uni-card v-for="(item, i) in classList" :key="item.id" ellipsis="{{2}}" :title="item.majorAndClass" :thumbnail="avatar"
			:sub-title="'id号：' + item.id" :extra="'班级人数：' + item.capacity">
			<view slot="actions" class="card-actions">
				<view class="card-actions-item" @click="lookRoster(item.id, item.majorAndClass)">
					<text class="card-actions-item-text">班级成员名单</text>
					<uni-icons type="arrow-right" size="18" color="#999"></uni-icons>
				</view>
			</view>
		</uni-card>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				avatar: "https://bkimg.cdn.bcebos.com/pic/b21c8701a18b87d6473830f5000828381e30fde6",
				classList: [],
				enterChoose:null
			}
		},
		onLoad(options) {
			this.enterChoose = options.choose
			this.requestClassList()
		},
		methods: {
			lookRoster(id,className){
				uni.navigateTo({
					url : `../classRoster/classRoster?id=${id}&class=${className}&choose=${this.enterChoose}`,
					animationType: 'pop-in',
					animationDuration: 200
				})
			},
			requestClassList(){
				if(this.enterChoose == 0){
					uni.$http.get("/teacher/getClassInfo").then(res => {
						if (res.data.code == 200) {
							uni.showToast({
								title: '加载中',
								duration: 500,
								icon: "loading"
							});
							this.classList = res.data.data
						} else {
							this.msg.msgType = "error"
							this.msg.messageText = res.data.message
							this.$refs.message.open()
						}
					}).catch(err => {
						this.msg.msgType = "error"
						this.msg.messageText = err.errMsg
						this.$refs.message.open()
					})
				}else if(this.enterChoose == 1){
					uni.$http.get("/teacher/getAllClass").then(res => {
						if (res.data.code == 200) {
							uni.showToast({
								title: '加载中',
								duration: 500,
								icon: "loading"
							});
							this.classList = res.data.data
						} else {
							this.msg.msgType = "error"
							this.msg.messageText = res.data.message
							this.$refs.message.open()
						}
					}).catch(err => {
						this.msg.msgType = "error"
						this.msg.messageText = err.errMsg
						this.$refs.message.open()
					})
				}
			}
		}
	}
</script>

<style lang="scss">
	.class-list-page{
		.card-actions-item {
			display: flex;
			justify-content: flex-end;
		}
	}
</style>
