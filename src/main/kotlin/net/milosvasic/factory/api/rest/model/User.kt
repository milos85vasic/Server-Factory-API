package net.milosvasic.factory.api.rest.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["username"]), UniqueConstraint(columnNames = ["email"])])
class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    @Column()
    var username: String? = null

    @NaturalId
    @Column(nullable = false)
    var email: String? = null

    @Column(nullable = false)
    var emailVerified = false

    var imageUrl: String? = null

    @JsonIgnore
    var password: String? = null

    @Enumerated(EnumType.STRING)
    var provider: AuthProvider? = null
    var providerId: String? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = [JoinColumn(name = "user_id")],
            inverseJoinColumns = [JoinColumn(name = "role_id")])
    var roles: Set<Role> = HashSet()

    constructor(name: String?, username: String?, email: String?, password: String?) {

        this.name = name
        this.username = username
        this.email = email
        this.password = password
    }

    constructor()
}