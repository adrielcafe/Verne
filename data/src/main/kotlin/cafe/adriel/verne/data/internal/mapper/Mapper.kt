package cafe.adriel.verne.data.internal.mapper

internal interface Mapper<I, O> {

    operator fun invoke(input: I): O
}
