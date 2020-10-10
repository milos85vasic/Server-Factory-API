package net.milosvasic.factory.api.rest.model

import javax.persistence.*

@Entity
@Table(name = "roles")
class RoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    var name: ERole? = null

    constructor() {}
    constructor(name: ERole?) {
        this.name = name
    }
}