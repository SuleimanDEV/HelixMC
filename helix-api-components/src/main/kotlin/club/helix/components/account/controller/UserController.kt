package club.helix.components.account.controller

import club.helix.components.HelixComponents
import club.helix.components.account.HelixUser

abstract class UserController(open val api: HelixComponents) {

    abstract fun load(name: String): HelixUser?
    abstract fun delete(name: String)
    abstract fun save(user: HelixUser)
}