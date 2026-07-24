'use client';

import { FormularioCadastro } from './formulario-cadastro';
import { GridExtrato } from '@/components/operador/grid-extrato';

export default function OperadorPage() {
  return (
    <main className="container mx-auto p-6 space-y-8 max-w-7xl">
      {/* Cabeçalho do Sistema Multimoedas */}
      <div className="border-b border-slate-200 pb-4">
        <h1 className="text-3xl font-bold tracking-tight text-slate-900">
          Motor de Recebíveis & Câmbio
        </h1>
        <p className="text-sm text-slate-500 mt-1">
          Operações estruturadas de Duplicatas e Cheques com liquidação atômica e proteção contra Race Conditions.
        </p>
      </div>

      {/* Seção 1: Painel do Operador (Formulário + Simulação) */}
      <section className="bg-white rounded-xl border border-slate-200 shadow-sm p-6">
        <h2 className="text-xl font-semibold text-slate-800 mb-4">Novo Recebível / Simulação</h2>
        <FormularioCadastro />
      </section>

      {/* Seção 2: Grid de Consulta CQRS (Tabela com Paginação Server-Side) */}
      <section className="bg-white rounded-xl border border-slate-200 shadow-sm p-6">
        <h2 className="text-xl font-semibold text-slate-800 mb-4">Extrato Analítico de Transações</h2>
        <GridExtrato />
      </section>
    </main>
  );
}
