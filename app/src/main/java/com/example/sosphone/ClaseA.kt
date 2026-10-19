package com.example.sosphone

class ClaseA {
    /* ATRIBUTOS */
    var ident: Int = 0
    private var nom: String = ""
    var actividad: Boolean = false

    /* CONSTRUCTOR */
    constructor() {
        this.ident = 0
        this.nom = ""
        this.actividad = false
    }

    constructor(id: Int, nombre: String, activo: Boolean) {
        this.ident = id
        this.nom = nombre
        this.actividad = activo
    }

    /* GETTERS Y SETTERS */
    fun getId(): Int {
        return this.ident
    }

    fun setId(id: Int) {
        this.ident = id
    }

    fun getNombre(): String {
        return this.nom
    }

    fun setNombre(nombre: String) {
        this.nom = nombre
    }
    fun getactivo(): Boolean {
        return this.actividad
    }

    fun setActivo(activo: Boolean) {
        this.actividad = activo
    }

    /* TOSTRING */
    override fun toString(): String {
        return "ClaseA [id=$ident, nombre=$nom, activo=$actividad]"
    }
}