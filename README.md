# Aplicativo de Lista de Compras
Desenvolver um Aplicativo de Lista de Compras para Android Nativo com Kotlin.

# Funcionalidades
## RF001 - Login / Logout
- Tela de Login: A tela inicial do aplicativo oferece campos para e-mail e senha, além do logotipo da aplicação.
- Validação: Verifica se o e-mail é válido e se os campos não estão vazios.
- Botões: Inclui botões para "Login" e "Cadastro de Novo Usuário".
- Logout: Ao realizar logout, os dados da sessão são descartados e a tela de login é reapresentada.
## RF002 - Cadastro de Usuário
- Cadastro: Permite o registro de novos usuários com os seguintes campos:
Nome
E-mail
Senha
Confirmação de Senha
- Validação: Verifica o preenchimento de todos os campos, a validade do e-mail e a correspondência entre a senha e a confirmação.
## RF003 - Gestão de Listas de Compras
-Criação e Edição: Permite criar e editar listas de compras, que incluem:
Título
Imagem (opcional, com a possibilidade de selecionar uma mídia do dispositivo)
- Listagem: Exibe listas ordenadas alfabeticamente, mostrando título e imagem. Se não houver imagem, um placeholder será exibido.
- Exclusão: Ao excluir uma lista de compras, todos os itens associados também serão excluídos.
## RF004 - Gestão de Itens da Lista
- Criação e Edição: Permite adicionar e modificar itens nas listas de compras, que incluem:
Nome
Quantidade
Unidade
Categoria (ex: fruta, carne)
- Listagem: Os itens são exibidos dentro de suas respectivas listas, com informações sobre nome, quantidade, unidade e ícone da categoria. São ordenados alfabeticamente e agrupados por categoria.
- Marcação de Itens: Itens podem ser marcados como comprados ou desmarcados. Itens marcados são exibidos separadamente dos demais, na parte inferior da lista.
## RF005 - Buscas
- Pesquisa: Permite buscar por listas de compras e itens dentro das listas. A pesquisa pode ser realizada pelo nome do item ou da lista.
