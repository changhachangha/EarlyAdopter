<template>
  <v-app id="inspire">
    <v-app-bar :elevation="1" density="prominent">
      <div
        style="
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
        ">
        <v-img
          :style="{ margin: 'auto' }"
          src="~/assets/images/undinaru.png"
          width="140"></v-img>
      </div style="
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
        ">
      <div style="width: 100%; height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
          padding: 0px 5%;">
        <v-text-field
          append-icon="mdi-magnify"
          density="compact"
          hint="검색어를 입력해주세요"
          variant="underlined"
          hide-details
          single-line></v-text-field>
      </div>
      <div style="
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
        ">
        <v-btn icon="mdi-theme-light-dark" @click="toggleTheme"> </v-btn>
      </div>
    </v-app-bar>

    <v-main>
      <v-container>
        <v-row v-for="(item, n) in datas" :key="n">
          <v-sheet class="pa-2 ma-2 mx-auto" width="800">
            <v-hover v-slot="{ isHovering, props }">
              <v-card class="mx-auto" max-width="800" v-bind="props">
                <v-img
                  :src="item.brandLogo"
                  aspect-ratio="1"
                  width="100"
                  alt="상품이미지"
                  class="pa-20"></v-img>

                <v-card-text>
                  <h2 class="text-h6 text-primary">{{ item.brandNm }}</h2>
                  {{ item.urlPath }}
                </v-card-text>

                <!-- <v-card-title>
                <v-rating
                  :model-value="4"
                  background-color="orange"
                  class="me-2"
                  color="orange"
                  dense
                  hover></v-rating>
                <span class="text-primary text-subtitle-2">64 Reviews</span>
              </v-card-title> -->

                <v-overlay
                  :model-value="isHovering"
                  class="align-center justify-center"
                  scrim="#036358"
                  contained>
                  <v-btn variant="flat" @click="onClickSite(item.urlPath)"
                    >사이트 이동</v-btn
                  >
                </v-overlay>
              </v-card>
            </v-hover>
          </v-sheet>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>
<script setup lang="ts">
// import MainCard from '~/components/MainCard.vue';

const {
  data: productData,
  pending,
  error,
} = await useAsyncData('count', () =>
  $fetch('http://203.229.246.216:8080/product/all')
);
console.log(productData);

const datas: { value: any }[] | unknown = productData.value.content;
datas.length = 20;
// if (datas) {
//   for (let key in datas) {
//     console.log(key);
//     const name = datas[key].brandNm;
//     const img = datas[key].brandLogo;
//     const path = datas[key].urlPath;
//     brandNm.push(name);
//     brandLogo.push(img);
//     brandLogo.push(img);
//   }
// }

console.log(datas);

import { useTheme } from 'vuetify';

const theme = useTheme();

function toggleTheme() {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark';
}

function onClickSite(a) {
  window.open(a, '_blank');
}
</script>

<style scoped>
#mainLayout {
  min-height: 10000px;
}
div::v-deep .v-app-bar{
  padding: 0px 15%;
}
</style>
