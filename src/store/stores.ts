import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const useTitleStore = defineStore('title',() =>{
    const title = ref('all');
    const computed_title = computed(() => title.value[0].toUpperCase() + title.value.substring(1));

    return {title,computed_title};
});