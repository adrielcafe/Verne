package cafe.adriel.verne.data

import cafe.adriel.verne.data.internal.repository.FileContentRepository
import cafe.adriel.verne.data.internal.repository.ItemRepository
import cafe.adriel.verne.data.internal.repository.SessionRepository
import cafe.adriel.verne.data.internal.utils.FileCounter
import cafe.adriel.verne.data.internal.utils.FileFinder
import cafe.adriel.verne.data.internal.utils.FirebasePathResolver
import cafe.adriel.verne.domain.service.FileContentService
import cafe.adriel.verne.domain.service.ItemService
import cafe.adriel.verne.domain.service.SessionService
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val dataModule = module {

    single {
        Firebase.firestore
    }

    single<SessionService> {
        SessionRepository()
    }

    single<ItemService> {
        ItemRepository(
            firestore = get(),
            fileFinder = get(),
            fileCounter = get(),
            pathResolver = get(),
            logger = get()
        )
    }

    single<FileContentService> {
        FileContentRepository(
            firestore = get(),
            pathResolver = get(),
            logger = get()
        )
    }

    single {
        FileFinder(
            config = get()
        )
    }

    single {
        FileCounter()
    }

    single {
        FirebasePathResolver(
            session = get()
        )
    }
}
