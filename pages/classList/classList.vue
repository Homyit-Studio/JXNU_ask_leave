<template>
	<view class="class-list-page">
		<uni-card v-for="(item, i) in classList" :title="item.majorAndClass" :thumbnail="avatar"
			:sub-title="item.classNumber" :key="item.classNumber" extra="总人数:34人">
			<view slot="actions" class="card-actions">
				<view class="card-actions-item" @click="lookRoster">
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
				classList: [{
						classNumber: 3234243,
						majorAndClass: "20级计科1班"
					},
					{
						classNumber: 3234243,
						majorAndClass: "20级计科1班"
					},
					{
						classNumber: 3234243,
						majorAndClass: "20级计科1班"
					},
					{
						classNumber: 3234243,
						majorAndClass: "20级计科1班"
					},
					{
						classNumber: 3234243,
						majorAndClass: "20级计科1班"
					}
				]
			}
		},
		onLoad() {

		},
		methods: {
			lookRoster(){
				uni.navigateTo({
					url : "../classRoster/classRoster",
					animationType: 'pop-in',
					animationDuration: 200
				})
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
