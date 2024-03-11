<script setup lang="ts">
import { useRoute } from 'vue-router';

const route = useRoute();
</script>

<template>
  <div>
    <h1>Nuxt Routing set up successfully123!</h1>
    <p>Current route: {{ route.path }}</p>
  </div>
</template>
