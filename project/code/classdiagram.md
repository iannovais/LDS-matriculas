```code
@startuml

class Usuario {
    # ARQUIVOUSUARIO : string

    - id : int
    - nome: string
    - login : string
    - senha : string
    - tipoUsuario : TipoUsuario

    + entrar(login : string, senha : string) : void
    + cadastrar(nome : string, login : string, senha : string, tipoUsuario : TipoUsuario) : Usuario
    + salvar() : void
    + carregar(login : string) : Usuario
    + loginExiste(login : string) : boolean
    + atualizar(idUsuario : int, novoNome : string, tipoUsuario : TipoUsuario) : void
}

class Aluno {
    - MAXOBRIGATORIAS : int 
    - MAXOPTATIVAS : int 

    + matricularEmDisciplina(disciplina : Disciplina) : void
    + cancelarMatricula(disciplina : Disciplina) : void
    + visualizarMatriculas() : void
    + listarAlunos() : void
    + consultarCobranca() : void
}

class Professor {

    + alunosMatriculadosNaDisciplina(idDisciplina : int) : List<Aluno>
    + listarProfessores() : void
    + existe(idProfessor : int) : boolean
}

class Secretaria {

    + atualizarInformacoesDisciplina(idDisciplina : int, novoNome : String, novoCusto : float, novaObrigatoriedade : boolean) : void
    + atualizarInformacoesProfessor(idProfessor : int, novoNome : string) : void
    + atualizarInformacoesAluno(idAluno : int, novoNome : string) : void
    + abrirPeriodoMatriculas() : void
    + fecharPeriodoMatriculasfecharPeriodoMatriculas() : void
}

class Disciplina {
    # ARQUIVODISCIPLINA : string
    - MINIMOALUNOS : int
    - LIMITEALUNOS : int
    
    - idDisciplina : int
    - nome : string
    - custo : float
    - idCurso : int
    - idProfessor : int
    - ehObrigatoria : bool
    - status : StatusDisciplina
    - numeroMatriculados : int

    + salvar() : void
    + listarDisciplinas() : void
    + carregarTodasDisciplinas() : List<Disciplina>
    + matricularAluno(aluno : Aluno) : boolean
    + cancelarMatriculaAluno (aluno : Aluno) : void
    + verificarQuantidadeAlunosDisciplina() : void
    + atualizarInformacoes(novoNome : string, novoCusto : float, novaObrigatoriedade : boolean) : void
    + listarDisciplinasDoProfessor(idProfessor : int) : void
}

class Matricula {
    # ARQUIVOMATRICULAS : string
    
    - idAluno : int
    - idDisciplina : int
    - ativa : boolean

    + abrirPeriodoMatriculas() : void
    + fecharPeriodoMatriculas() : void
    + salvar() : void
    + carregarTodasMatriculas() : List<Matricula>
    + atualizarArquivo(matriculas : List<Matricula>) : void
    + carregarDisciplinasDoAluno(idAluno : int) : List<Integer>
    + alunoJaMatriculado(idAluno : int, idDisciplina : int) : boolean
    + getAlunosMatriculadosNaDisciplina(idDisciplina : int) : List<Aluno>
}

class Curso {
    # ARQUIVOCURSO : string

    - idCurso : int
    - nome : string
    - creditos : int

    + salvar() : void
    + listarCursos() : void
    + existe(idCurso : int) : boolean
}

class Cobranca {
    - aluno : Aluno
    - disciplinasCobradas : List<Disciplina>
    - valorTotal : float

    + gerarCobranca() : boolean
    + calcularValorTotal() : float
    + consultarCobranca() : void
}

Usuario <|-- Aluno
Usuario <|-- Professor
Usuario <|-- Secretaria

Aluno "1" -- "0..*" Curso 
Aluno "3..60" -- "1..*" Disciplina 
Professor "1" -- "0..*" Disciplina 
Curso "1" *-- "1..*" Disciplina 
Aluno "1" o-- "0..*" Cobranca 
Aluno "1" -- "0..*" Matricula
Disciplina "1" -- "0..*" Matricula

@enduml
```