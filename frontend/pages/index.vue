<template>
  <v-app id="inspire">
    <v-app-bar :elevation="1" density="compact">
      <v-app-bar-title width="30">EarlyAdopter</v-app-bar-title>
      <v-spacer></v-spacer>

      <v-btn icon="mdi-theme-light-dark" @click="toggleTheme"> </v-btn>
    </v-app-bar>
    <v-card class="mx-auto ma-10" color="grey-lighten-3" width="100%">
      <v-card-text width="400">
        <v-text-field
          width="400"
          append-inner-icon="mdi-magnify"
          density="compact"
          label=""
          variant="solo"
          hide-details
          single-line></v-text-field>
      </v-card-text>
    </v-card>
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
:deep(.v-toolbar) {
  width: 60%;
}
</style>
