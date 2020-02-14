package cafe.adriel.verne.data

import cafe.adriel.verne.data.model.ExplorerFile
import cafe.adriel.verne.data.model.ExplorerFolder
import cafe.adriel.verne.data.repository.ExplorerFileRepository
import cafe.adriel.verne.data.repository.ExplorerFolderRepository
import cafe.adriel.verne.domain.repository.FileRepository
import cafe.adriel.verne.domain.repository.FolderRepository
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {

    single {
        ObjectBox.init(
            context = androidContext(),
            config = get()
        )
    }

    single(named<ExplorerFolder>()) {
        get<BoxStore>().boxFor<ExplorerFolder>()
    }

    single(named<ExplorerFile>()) {
        get<BoxStore>().boxFor<ExplorerFile>()
    }

    single<FolderRepository> {
        ExplorerFolderRepository(
            folderBox = get(named<ExplorerFolder>()),
            fileBox = get(named<ExplorerFile>())
        )
    }

    single<FileRepository> {
        ExplorerFileRepository(
            fileBox = get(named<ExplorerFile>())
        )
    }
}
