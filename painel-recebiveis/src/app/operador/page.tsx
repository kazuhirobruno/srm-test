'use client';

import { FormularioCadastro } from '@/components/operador/formulario-cadastro';
import { GridExtrato } from '@/components/operador/grid-extrato';
import Link from 'next/link';

export default function OperadorPage() {
  return (
    <main className="container mx-auto p-6 space-y-8 max-w-7xl">
     <div className="flex justify-between items-center border-b border-slate-200 dark:border-slate-800 pb-4">
      <div>
        <h1 className="text-3xl font-bold tracking-tight text-slate-900 dark:text-slate-50">
          Motor de Recebíveis & Câmbio
        </h1>
        <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
          Operações estruturadas com liquidação atômica e proteção contra Race Conditions.
        </p>
      </div>
      {/* Botão de Navegação */}
      <Link 
        href="/taxas" 
        className="px-4 py-2 text-sm font-medium text-slate-900 dark:text-slate-100 bg-slate-200 dark:bg-slate-800 hover:bg-slate-300 dark:hover:bg-slate-700 rounded-md transition-colors shadow-sm"
      >
        Gerenciar Taxas
      </Link>
    </div>

      <section className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 shadow-sm p-6">
        <h2 className="text-xl font-semibold text-slate-800 dark:text-slate-100 mb-4">Novo Recebível / Simulação</h2>
        <FormularioCadastro />
      </section>

      <section className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 shadow-sm p-6">
        <h2 className="text-xl font-semibold text-slate-800 dark:text-slate-100 mb-4">Extrato Analítico de Transações</h2>
        <GridExtrato />
      </section>
    </main>
  );
}
