package com.KtorChat.room

class MemberAlreadyExistsException: Exception(
    "Member with the same username already exists in the room."
)