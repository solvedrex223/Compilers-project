<template>
    <h1 class="code_list_title">{{ title }}</h1>
    <draggable
    :list="list"
    class="code_list"
    :id="id"
    :group="group"
    item-key="name"
    @change="$emit('list_update')">
            <template #item="{ element, index }">
                        <v-sheet
                        v-if="element.statement != undefined"
                        class="code_list_item cursor-move bg-transparent handle"
                        :class="`elevation-${isHovering ? 24 : 6}`"
                        :id="'item-'+index ">
                        <v-row no-gutters class="g4">
                            <v-col cols="2">
                                <v-sheet>{{ element.name }}</v-sheet>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field class="form-control" label="Code" clearable/>
                            </v-col>
                            <v-col cols="2">
                                <v-sheet>){</v-sheet>
                            </v-col>
                            <v-col cols="2">
                                <v-icon icon="mdi-close" @click="removeAt(index)"></v-icon>
                            </v-col>
                        </v-row>
                        </v-sheet>
                        <v-sheet 
                        v-else
                        class="code_list_item cursor-move bg-transparent handle"
                        :class="`elevation-${isHovering ? 24 : 6}`">{{element.name}}</v-sheet>
            </template>
    </draggable>
</template>

<script setup lang="ts">
import { defineComponent } from 'vue';
import Draggable from 'vuedraggable';

    defineComponent({
        Draggable
    });
    defineProps({
        id: String,
        title: {
            default: 'All',
            type: String,
        },
        list: {
            default(){return [
                { name: "begin", statement: undefined },
                { name: "end", statement: undefined }
            ]},
            type: Array
        },
        group: {}
    });
    function  removeAt(idx:number) {
      this.list.splice(idx, 1);
    }
</script>

<style>
    .code_list{
        position: relative;
        left:3%;
        padding: .5%;
        border-style: solid;
        border-color: white;
        border-width: 1px;
        max-width: 100%;
        margin-left: auto;
        margin-right: auto;
        overflow-y: auto;
        max-height: 80%;
    }
    .code_list_item{
        position: inherit;
        text-align: center;
        border-width: 1px;
        border-color: white;
        border-style: solid;
        font-size: 30px;
        min-height: 0;
        display: flex;
    }
    .code_list_title{
        position: relative;
        left:3%;
        text-align: center;
        padding-top: 1%;
    }
    .form-control{}
</style>