export default defineNuxtConfig({
  ssr: true,
  devtools: { enabled: true },
  css: ['vuetify/styles'], //
  modules: [
    'vuetify-nuxt-module',
    [
      '@pinia/nuxt',
      {
        autoImports: ['defineStore', 'acceptHMRUpdate'],
      },
    ],
  ], //설정
  vite: {
    define: {
      'process.env.DEBUG': false,
    },
  },
  vuetify: {
    //설정
    moduleOptions: {
      /* module specific options */
      styles: { configFile: '/settings.scss' },
    },
    vuetifyOptions: {
      /* vuetify options */
    },
  },
  features: {
    //설정
    inlineStyles: false,
  },
  vue: {
    runtimeCompiler: true,
  },
  sourcemap: {
    server: false,
    client: false,
  },
  build: {
    transpile: ['vuetify'],
  },
  imports: {
    dirs: ['/stores'],
  },
});
