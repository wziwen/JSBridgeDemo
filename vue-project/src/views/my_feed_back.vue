<template>
  <div class="feedback">
    <div class="input_text">
      <textarea v-model="content" class="feedback_input_text" v-bind:placeholder="$t('input place holder text')"></textarea>
    </div>
    <button class="feedback_submit_button" :disabled="disabled" :style="button_style" @click="feedback">{{ $t("submit") }}</button>
  </div>
</template>

<script>
  export default {
      data() {
          return {
            content:"",
            disable:true
          }
      },
      computed: {
          button_stype() {
              if (this.content) {
                  this.disabled = true
              } else {
                  this.disabled = false
              }
          }
      },
      methods: {
          feedback() {
              if (this.content && this.$store.state.userInfo.id) {
                  this.$http.post( this.$configs.api + 'help_and_feedback/feedback', {
                      id: this.$store.state.userInfo.id,
                      content: this.content
                  }).then((response) => {
                      this.$router.push({name:'user'})
                  }, (error) => {
                      console.error(error)
                  })
              } else {
                  console.log("用户未登陆或没有输入数据")
              }
          }
      }
  }
</script>
<style>
  .feedback {
    maring: 12px
  }
  .feedback .input_text {
    background: #fff;
    padding: 14px 12px;
  }
  .feedback_input_text {
    outline: none;
    width:100%;
    height: 200px;
    border: none;
    -webkit-border-radius: 10px;
    -moz-border-radius: 10px;
    border-radius: 10px;
    font-size: 16px;
    line-size:20px;
    resize: none;
  }
  .feedback_submit_button {
    width:100%;
    height: 36px;
    background-color: black;
    font-size: 16px;
    border-radius: 10px;
    top: 12px;
  }
  .feedback_submit_button:disabled {
    background-color: #C2C2C2;
  }
</style>
