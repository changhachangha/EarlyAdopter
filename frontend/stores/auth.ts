interface IUser {
  email: string;
  name: string;
}
// auth 스토어
export const useAuthStore = defineStore('auth', {
  state: () => {
    return {
      user: {
        email: '',
        name: '',
      } as IUser,
    };
  },
  getters: {
    getEmail: (state): string => state.user.email,
  },
  actions: {
    setUser(data: IUser) {
      this.user = data;
    },
  },
});

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useAuthStore, import.meta.hot));
}
