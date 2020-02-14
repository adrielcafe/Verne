package cafe.adriel.verne.data.mapper

internal interface Mapper<I, O> {

    suspend operator fun invoke(input: I): O
}
