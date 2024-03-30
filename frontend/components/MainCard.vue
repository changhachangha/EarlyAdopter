<template>
  <v-carousel cycle hide-delimiters>
    <v-carousel-item v-for="(item, i) in logos" :key="i" position="center">
    <img :src="item"></img>
    </v-carousel-item>
  </v-carousel>

</template>
<script setup>
const props = defineProps(['brandNm', 'brandLogo']);
props.brandLogo.length = 10;
const logos = props.brandLogo;

console.log(logos);
</script>

<style scoped>

</style>
