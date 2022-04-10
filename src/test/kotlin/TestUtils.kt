fun String.noWs() : String{
   return this.filter { !it.isWhitespace() }
}