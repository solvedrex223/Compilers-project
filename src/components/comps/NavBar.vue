<template>
    <v-app-bar :elevation="2">
        <template v-slot:prepend>
            <v-app-bar-nav-icon @click.stop="navbar = !navbar"></v-app-bar-nav-icon>
        </template>
        <v-app-bar-title>Scratch++</v-app-bar-title>
        <template v-slot:append>
            <v-btn>Graphic</v-btn>
            <v-btn>Text</v-btn>
        </template>
    </v-app-bar>
    <v-navigation-drawer v-model="navbar">
        <v-list>
            <v-list-item v-for="item in item_list" :key="item.value" :value="item.value" @click.stop="setTitle(item.value)">
                {{ item.title }}
            </v-list-item>
        </v-list>
    </v-navigation-drawer>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import { useTitleStore } from '../../store/stores';
    
    const title_store = useTitleStore();
    const navbar = ref(false);
    const item_list = [
        {title: 'All', value: 'all'},
        {title: 'Variables', value: 'variables'},
        {title: 'Syntax', value: 'syntax'},
        {title: 'Classes', value: 'classes'},
        {title: 'Functions', value: 'functions'}
    ];
    function setTitle(title:string) {
        title_store.title = title;
    }
</script>