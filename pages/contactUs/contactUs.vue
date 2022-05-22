<template>
	<view class="contact-us">
		<view class="homyit">
			<cover-image src="https://homyit-studio.github.io/Homyit-Guide/images/Homyit.png" class="homyit-image"></cover-image>
			<text>HOMYIT STUDIO</text>
		</view>
		<uni-list>
			<uni-list-item title="教师端手册" note="班主任权限:审批假条,管理班级"></uni-list-item>
			<uni-list-item  title="问题反馈" note="请联系234"></uni-list-item>
		</uni-list>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="scss">
	.contact-us{
		.homyit{
			margin: 20px 0;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			.homyit-image{
				width: 60px;
				height: 60px;
			}
		}
	}
</style>
