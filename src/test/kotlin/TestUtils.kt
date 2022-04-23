fun String.flat() : String{
   return this.filter { !it.isWhitespace() }
}
