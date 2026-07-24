'use client';

import { useExtrato } from '@/hooks/use-extrato';
import { TipoRecebivel } from '@/types/recebivel';

export function GridExtrato() {
  const { dados, carregando, erro, filtros, atualizarFiltros } = useExtrato();

  const mudarPagina = (novaPagina: number) => {
    if (dados && novaPagina >= 0 && novaPagina < dados.totalPages) {
      atualizarFiltros({ page: novaPagina });
    }
  };

  return (
    <div className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-4 gap-3 bg-slate-50 p-4 rounded-lg border border-slate-200">
        <div>
          <label className="block text-xs font-medium text-slate-600 mb-1">Cedente</label>
          <input
            type="text"
            value={filtros.cedente || ''}
            onChange={(e) => atualizarFiltros({ cedente: e.target.value })}
            className="w-full rounded-md border border-slate-300 p-2 text-xs focus:outline-none focus:border-indigo-500"
            placeholder="Filtrar por cedente..."
          />
        </div>

        <div>
          <label className="block text-xs font-medium text-slate-600 mb-1">Moeda</label>
          <select
            value={filtros.moeda || ''}
            onChange={(e) => atualizarFiltros({ moeda: e.target.value || undefined })}
            className="w-full rounded-md border border-slate-300 p-2 text-xs bg-white focus:outline-none focus:border-indigo-500"
          >
            <option value="">Todas as moedas</option>
            <option value="BRL">BRL</option>
            <option value="USD">USD</option>
          </select>
        </div>

        <div>
          <label className="block text-xs font-medium text-slate-600 mb-1">Data Início</label>
          <input
            type="date"
            value={filtros.dataInicio || ''}
            onChange={(e) => atualizarFiltros({ dataInicio: e.target.value || undefined })}
            className="w-full rounded-md border border-slate-300 p-2 text-xs focus:outline-none focus:border-indigo-500"
          />
        </div>

        <div>
          <label className="block text-xs font-medium text-slate-600 mb-1">Data Fim</label>
          <input
            type="date"
            value={filtros.dataFim || ''}
            onChange={(e) => atualizarFiltros({ dataFim: e.target.value || undefined })}
            className="w-full rounded-md border border-slate-300 p-2 text-xs focus:outline-none focus:border-indigo-500"
          />
        </div>
      </div>
      {erro && (
        <div className="p-3 bg-rose-50 border border-rose-200 text-rose-700 text-sm rounded-md">
          {erro}
        </div>
      )}
      <div className="overflow-x-auto rounded-lg border border-slate-200">
        <table className="w-full border-collapse text-left text-sm text-slate-500">
          <thead className="bg-slate-50 text-xs uppercase tracking-wider text-slate-700 border-b border-slate-200">
            <tr>
              <th className="px-6 py-3 font-semibold">ID</th>
              <th className="px-6 py-3 font-semibold">Cedente</th>
              <th className="px-6 py-3 font-semibold">Tipo</th>
              <th className="px-6 py-3 font-semibold">Valor Original</th>
              <th className="px-6 py-3 font-semibold">Valor Liquidado</th>
              <th className="px-6 py-3 font-semibold">Status</th>
              <th className="px-6 py-3 font-semibold">Vencimento</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-200 bg-white">
            {carregando ? (
              Array.from({ length: 3 }).map((_, idx) => (
                <tr key={idx} className="animate-pulse">
                  <td colSpan={7} className="px-6 py-4 text-center text-slate-400">
                    Carregando registros do motor financeiro...
                  </td>
                </tr>
              ))
            ) : dados && dados.content.length > 0 ? (
              dados.content.map((item) => (
                <tr key={item.id} className="hover:bg-slate-50 transition-colors">
                  <td className="px-6 py-4 font-medium text-slate-900">#{item.id}</td>
                  <td className="px-6 py-4">{item.cedente}</td>
                  <td className="px-6 py-4">
                    <span className={`inline-flex items-center rounded-md px-2 py-1 text-xs font-medium ${
                      item.tipo === TipoRecebivel.DUPLICATA ? 'bg-blue-50 text-blue-700' : 'bg-purple-50 text-purple-700'
                    }`}>
                      {item.tipo}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    {item.moedaOriginal} {item.valorOriginal.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                  </td>
                  <td className="px-6 py-4 font-semibold text-slate-900">
                    {item.moedaLiquidacao} {item.valorLiquidado.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                  </td>
                  <td className="px-6 py-4">
                    <span className={`inline-flex items-center rounded-md px-2 py-1 text-xs font-medium ${
                      item.status === 'LIQUIDADO' ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' : 'bg-amber-50 text-amber-700 border border-amber-200'
                    }`}>
                      {item.status}
                    </span>
                  </td>
                  <td className="px-6 py-4 text-slate-400">
                    {new Date(item.vencimento + 'T00:00:00').toLocaleDateString('pt-BR')}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={7} className="px-6 py-10 text-center text-slate-400 italic">
                  Nenhuma transação encontrada para os filtros aplicados.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
      {dados && dados.totalPages > 1 && (
        <div className="flex items-center justify-between border-t border-slate-200 pt-4">
          <div className="text-xs text-slate-500">
            Mostrando página <span className="font-semibold text-slate-700">{dados.number + 1}</span> de{' '}
            <span className="font-semibold text-slate-700">{dados.totalPages}</span> ({dados.totalElements} títulos)
          </div>
          <div className="flex gap-2">
            <button
              onClick={() => mudarPagina(dados.number - 1)}
              disabled={dados.number === 0 || carregando}
              className="px-3 py-1.5 text-xs font-medium text-slate-700 bg-white border border-slate-300 rounded-md hover:bg-slate-50 transition-colors disabled:opacity-40"
            >
              Anterior
            </button>
            <button
              onClick={() => mudarPagina(dados.number + 1)}
              disabled={dados.number + 1 >= dados.totalPages || carregando}
              className="px-3 py-1.5 text-xs font-medium text-slate-700 bg-white border border-slate-300 rounded-md hover:bg-slate-50 transition-colors disabled:opacity-40"
            >
              Próximo
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
