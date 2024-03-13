<template>
    <v-container>
        <v-row no-gutters id="row_code">
            <v-col>
                <DragList :id="'syntax_list'" :title = "computed_title" :list="list" :group="syn_group"/>
            </v-col>
            
            <v-col>
                <DragList :id="'code_list'" title="Code" :list="list_code" :group="'name'"/>
            </v-col>
        </v-row>
    </v-container>
</template>
<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { ref, watch } from 'vue';
import { useTitleStore } from '../store/stores';

    const syntax_list = [
        { name: "{" },
        { name: "}" },
        { name: ")" },
        { name: ";" },
        { name: "begin" },
        { name: "end" },
        { name: "if(" },
        { name: "else(" },
        { name: "while(" },
    ];
    const var_list = [
        { name: "int" },
        { name: "float" },
        { name: "String" },
    ];
    const default_code = [{name: 'begin'}, {name: 'end'}];
    const syn_group = { name: 'name', pull: 'clone', put: false };
    const class_list = [];
    const {computed_title,title} = storeToRefs(useTitleStore());
    const list = ref([...syntax_list, ...class_list, ...var_list]);
    const list_code = ref(default_code);

    

    watch(title,(title) => {
        switch (title) {
            case 'all':
                list.value = [...syntax_list, ...class_list, ...var_list];
                break;
            case 'syntax':
                list.value = syntax_list;
                break;
            case 'variables':
                list.value = var_list;
                break;
        }
    });
</script>

<style>
    #space{
        max-width: 50%;
    }
    #row_code{
        margin-left: 20%;
        margin-right: 20%;
        max-height: 70%;
    }
</style>