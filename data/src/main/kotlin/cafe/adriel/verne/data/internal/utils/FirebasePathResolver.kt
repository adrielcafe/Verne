package cafe.adriel.verne.data.internal.utils

import cafe.adriel.verne.domain.service.SessionService

internal class FirebasePathResolver(private val session: SessionService) {

    fun getItemsPath() =
        PATH_ITEMS.format(session.userId)

    fun getFileContentPath(contentId: String) =
        PATH_FILE_CONTENT.format(session.userId, contentId)

    private companion object {

        const val PATH_ITEMS = "items/%s"
        const val PATH_FILE_CONTENT = "$PATH_ITEMS/contents/%s"
    }
}
