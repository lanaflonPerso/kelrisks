<template>
    <div class="leaflet">
        <l-map :center="center"
               :zoom="zoom"
               ref="leafletMap">
            <l-tile-layer :url="url"></l-tile-layer>
            <l-geo-json :geojson="JSON.parse(data.adresse)"
                        v-if="data.adresse"
                        :options="adresseOptions"></l-geo-json>
            <l-geo-json :geojson="JSON.parse(data.parcelle)"
                        :options="parcelleOptions"
                        :options-style="parcelleStyleFunction"></l-geo-json>
            <l-geo-json :geojson="data.basias.map(x => JSON.parse(x))"
                        :options="basiasOptions"
                        :options-style="basiasStyleFunction"></l-geo-json>
            <l-geo-json :geojson="data.basol.map(x => JSON.parse(x))"
                        :options="basolOptions"
                        :options-style="basolStyleFunction"></l-geo-json>
            <l-geo-json :geojson="data.sis.map(x => JSON.parse(x))"
                        :options-style="sisStyleFunction"></l-geo-json>
            <l-geo-json :geojson="data.icpe.map(x => JSON.parse(x))"
                        :options="icpeOptions"
                        :options-style="icpeStyleFunction"></l-geo-json>
            <l-geo-json :geojson="data.ssp.map(x => JSON.parse(x))"></l-geo-json>
        </l-map>
    </div>
</template>

<script>
import {icon, marker} from "leaflet";
import {LGeoJson, LMap, LTileLayer} from 'vue2-leaflet';

export default {
    name: "Leaflet",
    components: {
        LMap,
        LTileLayer,
        LGeoJson
    },
    props: {
        center: {
            type: Array,
            default: () => [0, 0]
        },
        data: {
            type: Object,
            default: () => {
                return {
                    parcelle: "",
                    basias: "",
                    basol: "",
                    sis: "",
                    icpe: "",
                    ssp: ""
                }
            }
        }
    },
    data: () => ({
        url: 'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
        zoom: 16,
        bounds: null
    }),
    methods: {
        crippleMap (map) {
            map.zoomControl.disable()
            map.touchZoom.disable()
            map.doubleClickZoom.disable()
            map.scrollWheelZoom.disable()
            map.boxZoom.disable()
            map.keyboard.disable()

            map.dragging.disable()
        }
    },
    computed: {
        parcelleOptions () {
            return {
                onEachFeature: this.onEachFeatureFunction,
            };
        },
        icpeOptions () {
            return {
                onEachFeature: this.onEachFeatureFunction,
                pointToLayer: this.createIcpeIcon
            };
        },
        basiasOptions () {
            return {
                onEachFeature: this.onEachFeatureFunction,
                pointToLayer: this.createBasiasIcon
            };
        },
        basolOptions () {
            return {
                onEachFeature: this.onEachFeatureFunction,
                pointToLayer: this.createBasolIcon
            };
        },
        adresseOptions () {
            return {
                onEachFeature: this.onEachFeatureFunction,
                pointToLayer: this.createAdresseIcon
            };
        },
        parcelleStyleFunction () {
            // const fillColor = this.fillColor; // important! need touch fillColor in computed for re-calculate when change fillColor
            return () => {
                return {
                    weight: 2,
                    color: "#455674",
                    opacity: 0.8,
                    fillColor: "#455674",
                    fillOpacity: 0.2
                };
            };
        },
        basiasStyleFunction () {
            return () => {
                return {
                    weight: 2,
                    color: "#EFEF00",
                    opacity: 0.8,
                    fillColor: "#EFEF00",
                    fillOpacity: 0.2
                };
            };
        },
        basolStyleFunction () {
            return () => {
                return {
                    weight: 2,
                    color: "#FF9800",
                    opacity: 0.8,
                    fillColor: "#FF9800",
                    fillOpacity: 0.2
                };
            };
        },
        sisStyleFunction () {
            return () => {
                return {
                    weight: 2,
                    color: "#C500FF",
                    opacity: 0.8,
                    fillColor: "#C500FF",
                    fillOpacity: 0.2
                };
            };
        },
        icpeStyleFunction () {
            return () => {
                return {
                    weight: 2,
                    color: "#C00900",
                    opacity: 0.8,
                    fillColor: "#C00900",
                    fillOpacity: 0.2
                };
            };
        },
        onEachFeatureFunction () {
            return (feature, layer) => {
                layer.bindTooltip(
                    () => {
                        let divs = ''
                        for (let property in feature.properties) {
                            let value = feature.properties[property]
                            let label = property.replace(/([A-Z])/gm, function (v) {
                                return ' ' + v.toLowerCase()
                            }).replace(/(^.)/gm, function (v) {
                                return v.toUpperCase()
                            })
                            divs = divs.concat('<div>', label, ' : ', value, '</div>')
                        }

                        return divs
                    },
                    {permanent: false, sticky: true}
                );
            };
        },
        createAdresseIcon () {
            return (feature, latlng) => {
                let myIcon = icon({
                    iconUrl: '/images/leaflet/adresse.svg',
                    shadowUrl: '/images/leaflet/shadow.png',
                    iconSize: [35, 35], // width and height of the image in pixels
                    shadowSize: [30, 22], // width, height of optional shadow image
                    iconAnchor: [17, 35], // point of the icon which will correspond to marker's location
                    shadowAnchor: [0, 24],  // anchor point of the shadow. should be offset
                    popupAnchor: [0, 0] // point from which the popup should open relative to the iconAnchor
                })
                return marker(latlng, {icon: myIcon})
            };
        },
        createIcpeIcon () {
            return (feature, latlng) => {
                let myIcon = icon({
                    iconUrl: '/images/leaflet/icpe.svg',
                    shadowUrl: '/images/leaflet/shadow.png',
                    iconSize: [35, 35], // width and height of the image in pixels
                    shadowSize: [30, 22], // width, height of optional shadow image
                    iconAnchor: [17, 35], // point of the icon which will correspond to marker's location
                    shadowAnchor: [0, 24],  // anchor point of the shadow. should be offset
                    popupAnchor: [0, 0] // point from which the popup should open relative to the iconAnchor
                })
                return marker(latlng, {icon: myIcon})
            };
        },
        createBasiasIcon () {
            return (feature, latlng) => {
                let myIcon = icon({
                    iconUrl: '/images/leaflet/basias.svg',
                    shadowUrl: '/images/leaflet/shadow.png',
                    iconSize: [35, 35], // width and height of the image in pixels
                    shadowSize: [30, 22], // width, height of optional shadow image
                    iconAnchor: [17, 35], // point of the icon which will correspond to marker's location
                    shadowAnchor: [0, 24],  // anchor point of the shadow. should be offset
                    popupAnchor: [0, 0] // point from which the popup should open relative to the iconAnchor
                })
                return marker(latlng, {icon: myIcon})
            };
        },
        createBasolIcon () {
            return (feature, latlng) => {
                let myIcon = icon({
                    iconUrl: '/images/leaflet/basol.svg',
                    shadowUrl: '/images/leaflet/shadow.png',
                    iconSize: [35, 35], // width and height of the image in pixels
                    shadowSize: [30, 22], // width, height of optional shadow image
                    iconAnchor: [17, 35], // point of the icon which will correspond to marker's location
                    shadowAnchor: [0, 24],  // anchor point of the shadow. should be offset
                    popupAnchor: [0, 0] // point from which the popup should open relative to the iconAnchor
                })
                return marker(latlng, {icon: myIcon})
            };
        }
    },
    mounted () {
        this.$nextTick(() => {
            const map = this.$refs.leafletMap.mapObject
            map.LGeoJson()
            // console.log(map)
            this.crippleMap(map)
        })
    }
}
</script>

<style scoped>
    .leaflet {
        width  : 100%;
        height : 100%;
    }
</style>