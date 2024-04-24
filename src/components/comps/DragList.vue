<template>
    <h1 class="code_list_title">{{ title }}</h1>
    <draggable :list="list" class="code_list" :id="id" :group="group" item-key="name" @change="$emit('list_update')">
        <template #item="{ element, index }">
            <v-sheet v-if="element.statement != undefined && checkName(element.name)"
                class="code_list_item cursor-move bg-transparent handle" :id="'item-' + index">
                <v-row no-gutters class="g4">
                    <v-col cols="2">
                        <v-sheet class="code_text">{{ element.name }}</v-sheet>
                    </v-col>
                    <v-col cols="6">
                        <v-text-field class="form-control" label="Code" :rules="getTest(element.name)" :disabled="!remove" clearable v-model="element.statement" />
                    </v-col>
                    <v-col cols="1">
                        <v-sheet class="code_text">{{ getText(element.name) }}</v-sheet>
                    </v-col>
                    <v-col v-if="remove" cols="1">
                        <v-icon icon="mdi-close" @click="removeAt(index)"></v-icon>
                    </v-col>
                </v-row>
            </v-sheet>
            <v-sheet v-else-if="element.statement != undefined" class="code_list_item cursor-move bg-transparent handle"
                :id="'item-' + index">
                <v-row no-gutters class="g4">
                    <v-col cols="2">
                        <v-sheet class="code_text">{{ element.name }}</v-sheet>
                    </v-col>
                    <v-col cols="3">
                        <v-text-field class="form-control" label="Name" :rules="string_rules" :disabled="!remove" clearable v-model="element.var"/>
                    </v-col>
                    <v-col cols="1">
                        <v-sheet class="code_text">=</v-sheet>
                    </v-col>
                    <v-col cols="4">
                        <v-text-field class="form-control" label="Code" :rules="getTest(element.name)" :disabled="!remove" clearable v-model="element.statement"/>
                    </v-col>
                    <v-col cols="1">
                        <v-sheet class="code_text">{{ getText(element.name) }}</v-sheet>
                    </v-col>
                    <v-col v-if="remove" cols="1">
                        <v-icon icon="mdi-close" @click="removeAt(index)"></v-icon>
                    </v-col>
                </v-row>
            </v-sheet>
            <v-sheet v-else class="code_list_item cursor-move bg-transparent handle">{{ element.name }}</v-sheet>
        </template>
    </draggable>
</template>

<script setup lang="ts">
    import { defineComponent } from "vue";
import Draggable from "vuedraggable";

    defineComponent({
        Draggable,
    });
    const list = defineModel();
    
    defineProps({
        id: String,
        title: {
            default: "All",
            type: String,
        },
        group: {},
        remove: {
            type: Boolean,
            default() {
                return false;
            }
        }
    });

    const int_rules = [(v) => /^[0-9]+$/gm.test(v) || "Value must be an integer"];
    const string_rules = [(v) => /.+/gm.test(v) || "Value must be a string"];
    const float_rules = [(v) => /^\d+(\.\d+)?$/.test(v) || "Value must be a number"];
    const comparison_rules = [(v) => /^.+\s+([><]|(==)|(!=)|(>=)|(<=))\s+\w+$/.test(v) || "Invalid comparison"];

    function getTest(name: string) {
        switch (name) {
            case "int":
                return int_rules;
            case "float":
                return float_rules;
            case "String":
                return string_rules;
            case "if (":
            case "while (":
                return comparison_rules;
            default:
                return string_rules;
        }
    }

    function getText(name: string) {
        switch (name) {
            case "int":
            case "float":
            case "String":
                return ";";
            case "new":
            case "class":
                return "{";
            case "print(":
                return ");"
            default:
                return "){";
        }
    }
    function checkName(name:string){
        switch (name) {
            case "int":
            case "float":
            case "String":
                return false;
            default:
                return true;
        }
    }
</script>

<style>
    .code_list {
        position: relative;
        left: 3%;
        padding: 0.5%;
        border-style: solid;
        border-color: white;
        border-width: 1px;
        max-width: 100%;
        margin-left: auto;
        margin-right: auto;
        overflow-y: auto;
        max-height: 80%;
    }

    .code_list_item {
        position: inherit;
        text-align: center;
        border-width: 1px;
        border-color: white;
        border-style: solid;
        font-size: 30px;
        min-height: 0;
        display: flex;
    }

    .code_list_title {
        position: relative;
        left: 3%;
        text-align: center;
        padding-top: 1%;
    }

    .code_text {
        padding-bottom: 7.5%;
    }
</style>
