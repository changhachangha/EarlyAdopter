<template>
  마우스 위치: {{ x }}, {{ y }}, {{ name }}
  <v-app>
    <v-container class="titleBox">
      <v-combobox
        label="검색창"
        :items="brandNm"
        variant="solo-filled"></v-combobox>
    </v-container>
    <!-- <MainCard :brandNm="brandNm" :brandLogo="brandLogo"></MainCard> -->
  </v-app>
</template>
<script setup lang="ts">
// import MainCard from '~/components/MainCard.vue';
import { useMouse } from '~/mouse';
let name = ref('오리지널 부모');

const { x, y } = useMouse();
// const { data, pending, error, refresh } = await useAsyncData(() =>
//   $fetch('http://203.229.246.216:8080/product/all')
// );

const brandNm: String[] = [];
const brandLogo: String[] = [];
// const datas = data.value;
// console.log(datas);
// for (let key in datas) {
//   const name = datas[key].brandNm;
//   const img = datas[key].brandLogo;
//   brandNm.push(name);
//   brandLogo.push();
// }
</script>

<style scoped>
.titleBox {
  margin-top: 100px;
}
</style>
